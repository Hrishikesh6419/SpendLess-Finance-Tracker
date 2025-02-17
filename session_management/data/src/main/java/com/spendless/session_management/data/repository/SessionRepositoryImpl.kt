package com.spendless.session_management.data.repository

import android.util.Log
import androidx.datastore.core.DataStore
import com.hrishi.core.domain.preference.repository.UserPreferencesRepository
import com.hrishi.core.domain.utils.Result
import com.spendless.session_management.data.SessionPreferences
import com.spendless.session_management.data.utils.toDomain
import com.spendless.session_management.data.utils.toProto
import com.spendless.session_management.domain.model.SessionData
import com.spendless.session_management.domain.repository.SessionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class SessionRepositoryImpl(
    private val dataStore: DataStore<SessionPreferences>,
    private val preferencesRepository: UserPreferencesRepository
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
                    System.currentTimeMillis() + (userPreference.data.sessionDuration.getValueInLong() * MINUTES_TO_MILLIS)

                Log.d(
                    TAG,
                    "Saving session with expiration at ${formatTime(expirationTime)}"
                )

                dataStore.updateData { prefs ->
                    // ✅ Save session data with updated expiration time
                    sessionData.copy(sessionExpiryTime = expirationTime).toProto()
                }
            }

            is Result.Error -> Unit
        }
    }

    override suspend fun clearSession() {
        Log.d(
            TAG,
            "Clearing session expiration at ${formatTime(System.currentTimeMillis())}"
        )

        // ✅ Reset session data to default
        dataStore.updateData { SessionPreferences.getDefaultInstance() }
    }


    private suspend fun setSessionToExpired() {
        Log.d(TAG, "setSessionToExpired at ${formatTime(System.currentTimeMillis())}")

        // Reset session data to default
        dataStore.updateData { prefs ->

            prefs.toBuilder()
                .setSessionExpiryTime(0L)
                .build()
        }
    }

    /**
     * Retrieves the current session data as a Flow.
     */
    override fun getSessionData(): Flow<SessionData> {
        return dataStore.data.map { prefs ->
            Log.d(TAG, "Fetching session data: $prefs")
            prefs.toDomain()
        }
    }


    override fun isSessionExpired(): Flow<Boolean> {
        return dataStore.data.map { prefs ->
            val isExpired = System.currentTimeMillis() >= prefs.sessionExpiryTime
            Log.d(
                TAG,
                "Checking session expired: $isExpired at ${formatTime(System.currentTimeMillis())}"
            )
            Log.d(TAG, "Session expires at ${formatTime(prefs.sessionExpiryTime)}")
            if (isExpired) {
                setSessionToExpired()
            }
            isExpired
        }
    }

    override suspend fun checkAndUpdateSessionExpiry(): Boolean {
        val isSessionExpired = isSessionExpired().first()
        if (isSessionExpired) {
            Log.d(TAG, "Session expired. Updating DataStore.")
            setSessionToExpired()
        }
        return isSessionExpired
    }

    override suspend fun resetSessionExpiry() {
        dataStore.updateData { prefs ->
            val userPreference = preferencesRepository.getPreferences(prefs.userId).first()
            when (userPreference) {
                is Result.Success -> {
                    val sessionExpiryDurationMins =
                        userPreference.data.sessionDuration.getValueInLong()
                    val newExpirationTime =
                        System.currentTimeMillis() + (sessionExpiryDurationMins * MINUTES_TO_MILLIS)

                    Log.d(
                        TAG,
                        "Resetting session expiry to ${formatTime(newExpirationTime)} using stored duration: $sessionExpiryDurationMins mins"
                    )

                    prefs.toBuilder()
                        .setSessionExpiryTime(newExpirationTime)
                        .build()
                }

                is Result.Error -> prefs
            }
        }
    }
}