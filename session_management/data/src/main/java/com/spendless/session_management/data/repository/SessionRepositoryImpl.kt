package com.spendless.session_management.data.repository

import android.util.Log
import androidx.datastore.core.DataStore
import com.hrishi.core.domain.preference.repository.UserPreferencesRepository
import com.hrishi.core.domain.time.TimeProvider
import com.hrishi.core.domain.utils.Result
import com.hrishi.core.domain.utils.toEpochMillis
import com.spendless.session_management.data.SessionPreferences
import com.spendless.session_management.data.utils.toDomain
import com.spendless.session_management.data.utils.toProto
import com.spendless.session_management.domain.model.SessionData
import com.spendless.session_management.domain.repository.SessionRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.coroutines.cancellation.CancellationException

class SessionRepositoryImpl(
    private val dataStore: DataStore<SessionPreferences>,
    private val preferencesRepository: UserPreferencesRepository,
    private val timeProvider: TimeProvider
) : SessionRepository {

    companion object {
        private const val MINUTES_TO_MILLIS = 1000L
        private const val TAG = "hrishiii"

        // Date formatter for readable logs
        private val dateFormat = SimpleDateFormat("HH:mm:ss dd/MM/yyyy", Locale.US)

        private fun formatTime(timestamp: Long): String {
            return if (timestamp > 0) dateFormat.format(Date(timestamp)) else "N/A"
        }
    }

    override suspend fun saveSession(sessionData: SessionData) {
        try {
            val userPreference = preferencesRepository.getPreferences(sessionData.userId).first()
            if (userPreference is Result.Success) {
                val expirationTime =
                    timeProvider.currentLocalDateTime.toEpochMillis() +
                            (userPreference.data.sessionDuration.getValueInLong() * MINUTES_TO_MILLIS)

                Log.d(TAG, "Saving session with expiration at ${formatTime(expirationTime)}")

                withContext(Dispatchers.IO) {
                    dataStore.updateData { prefs ->
                        sessionData.copy(sessionExpiryTime = expirationTime).toProto()
                    }
                }
            }
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            Log.d(TAG, "Error saving session: ${e.message}")
        }
    }


    override suspend fun clearSession() {
        Log.d(
            TAG,
            "Clearing session expiration at ${formatTime(timeProvider.currentLocalDateTime.toEpochMillis())}"
        )

        withContext(Dispatchers.IO) {
            dataStore.updateData { SessionPreferences.getDefaultInstance() }
        }
    }

    override suspend fun setSessionToExpired() {
        withContext(Dispatchers.IO) {
            dataStore.updateData { prefs ->
                val isValidUser = prefs.userId > 0L
                if (!isValidUser) return@updateData prefs

                Log.d(
                    TAG,
                    "setSessionToExpired at ${formatTime(timeProvider.currentLocalDateTime.toEpochMillis())}"
                )
                prefs.toBuilder().setSessionExpiryTime(0L).build()
            }
        }
    }

    override fun getSessionData(): Flow<SessionData> {
        return dataStore.data
            .map { prefs -> prefs.toDomain() }
            .flowOn(Dispatchers.IO)
    }

    override fun isSessionExpired(): Flow<Boolean> {
        return dataStore.data
            .map { prefs ->
                val hasValidUser = prefs.userId > 0L
                val isExpired =
                    timeProvider.currentLocalDateTime.toEpochMillis() >= prefs.sessionExpiryTime

                if (!hasValidUser) {
                    Log.d(TAG, "No valid user session found. Returning expired=false.")
                    return@map false
                }

                Log.d(
                    TAG,
                    "Checking session expired: $isExpired at ${formatTime(timeProvider.currentLocalDateTime.toEpochMillis())}"
                )
                Log.d(TAG, "Session expires at ${formatTime(prefs.sessionExpiryTime)}")

                if (isExpired) {
                    setSessionToExpired()
                }
                isExpired
            }
            .flowOn(Dispatchers.IO)
    }

    override suspend fun resetSessionExpiry() {
        try {
            withContext(Dispatchers.IO) {
                dataStore.updateData { prefs ->
                    val currentTime = timeProvider.currentLocalDateTime.toEpochMillis()
                    val userPreference =
                        preferencesRepository.getPreferences(prefs.userId).firstOrNull()

                    if (userPreference !is Result.Success) {
                        Log.d(TAG, "Failed to fetch user preferences. Keeping old expiry.")
                        return@updateData prefs
                    }

                    val newSessionExpiryDurationMins =
                        userPreference.data.sessionDuration.getValueInLong()
                    val newExpirationTime =
                        currentTime + (newSessionExpiryDurationMins * MINUTES_TO_MILLIS)

                    Log.d(
                        TAG,
                        "Resetting session expiry from ${formatTime(prefs.sessionExpiryTime)} to ${
                            formatTime(newExpirationTime)
                        }"
                    )

                    prefs.toBuilder().setSessionExpiryTime(newExpirationTime).build()
                }
            }
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            Log.d(TAG, "Error resetting session expiry: ${e.message}")
        }
    }
}