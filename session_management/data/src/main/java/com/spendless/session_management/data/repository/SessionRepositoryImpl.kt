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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

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
        val userPreference = preferencesRepository.getPreferences(sessionData.userId).first()
        when (userPreference) {
            is Result.Success -> {
                val expirationTime =
                    timeProvider.currentLocalDateTime.toEpochMillis() + (userPreference.data.sessionDuration.getValueInLong() * MINUTES_TO_MILLIS)

                Log.d(
                    TAG,
                    "Saving session with expiration at ${formatTime(expirationTime)}"
                )

                dataStore.updateData { prefs ->
                    // Save session data with updated expiration time
                    sessionData.copy(sessionExpiryTime = expirationTime).toProto()
                }
            }

            is Result.Error -> Unit
        }
    }

    override suspend fun clearSession() {
        Log.d(
            TAG,
            "Clearing session expiration at ${formatTime(timeProvider.currentLocalDateTime.toEpochMillis())}"
        )

        // Reset session data to default
        dataStore.updateData { SessionPreferences.getDefaultInstance() }
    }


    override suspend fun setSessionToExpired() {

        // Reset session data to default
        dataStore.updateData { prefs ->
            val isValidUser = prefs.userId > 0L
            if (!isValidUser) {
                return@updateData prefs
            }

            Log.d(
                TAG,
                "setSessionToExpired at ${formatTime(timeProvider.currentLocalDateTime.toEpochMillis())}"
            )
            prefs.toBuilder()
                .setSessionExpiryTime(0L)
                .build()
        }
    }

    override fun getSessionData(): Flow<SessionData> {
        return dataStore.data.map { prefs ->
            prefs.toDomain()
        }
    }


    override fun isSessionExpired(): Flow<Boolean> {
        return dataStore.data.map { prefs ->
            val hasValidUser = prefs.userId > 0L
            val isExpired =
                timeProvider.currentLocalDateTime.toEpochMillis() >= prefs.sessionExpiryTime

            if (!hasValidUser) {
                Log.d(TAG, "No valid user session found. Returning expired=false.")
                return@map false  // User is not logged in, no need to show PIN prompt
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
    }

    override suspend fun resetSessionExpiry() {
        dataStore.updateData { prefs ->
            val currentTime = timeProvider.currentLocalDateTime.toEpochMillis()
            val userPreference = preferencesRepository.getPreferences(prefs.userId).firstOrNull()
            if (userPreference !is Result.Success) {
                Log.d(TAG, "Failed to fetch user preferences. Keeping old expiry.")
                return@updateData prefs
            }

            val newSessionExpiryDurationMins = userPreference.data.sessionDuration.getValueInLong()
            val newExpirationTime = currentTime + (newSessionExpiryDurationMins * MINUTES_TO_MILLIS)

            Log.d(
                TAG,
                "Resetting session expiry from ${formatTime(prefs.sessionExpiryTime)} to ${
                    formatTime(
                        newExpirationTime
                    )
                }"
            )

            prefs.toBuilder()
                .setSessionExpiryTime(newExpirationTime)
                .build()
        }
    }
}