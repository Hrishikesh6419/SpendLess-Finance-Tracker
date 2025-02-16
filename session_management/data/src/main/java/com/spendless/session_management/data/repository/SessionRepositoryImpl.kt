package com.spendless.session_management.data.repository

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import com.spendless.session_management.domain.repository.SessionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.max

class SessionRepositoryImpl(
    private val dataStore: DataStore<Preferences>
) : SessionRepository {

    override suspend fun startSession() {
        val durationMinutes: Long = 1
        val expirationTime = System.currentTimeMillis() + (durationMinutes * MINUTES_TO_MILLIS)
        Log.d(
            TAG,
            "Setting session expiration: ${formatTime(expirationTime)} ($durationMinutes min)"
        )

        dataStore.edit { preferences ->
            preferences[SESSION_EXPIRATION_TIME] = expirationTime
        }
    }

    override suspend fun clearSession() {
        Log.d(TAG, "Clearing session expiration at ${formatTime(System.currentTimeMillis())}")
        dataStore.edit { preferences ->
            preferences[SESSION_EXPIRATION_TIME] = 0L
        }
    }

    // TODO: Fix the following issue:
    /**
     * The above works, but in the edge case where the session expires but isn't refreshed.
     * It will not navigate to the pin screen again because isSessionExpired isn't emitted back to mainViewModel
     * As there was no change
     * The below works, but it will emit the value twice
     */
//    override suspend fun clearSession() {
//        Log.d(TAG, "Clearing session expiration at ${formatTime(System.currentTimeMillis())}")
//
//        dataStore.edit { preferences ->
//            preferences[SESSION_EXPIRATION_TIME] = -1L // Temporary value to trigger DataStore update
//        }
//
//        delay(100) // Give DataStore time to register the change
//
//        dataStore.edit { preferences ->
//            preferences.remove(SESSION_EXPIRATION_TIME) // Now remove the key to ensure `isSessionExpired()` detects the change
//        }
//    }


    override fun isSessionExpired(): Flow<Boolean> {
        return getRemainingSessionTime().map { remainingTimeMs ->
            val isExpired = remainingTimeMs <= 0
            Log.d(
                TAG,
                "Checking session expired: $isExpired at ${formatTime(System.currentTimeMillis())}"
            )
            isExpired
        }
    }


    override suspend fun checkAndUpdateSessionExpiry() {
        if (isSessionExpired().first()) {
            Log.d(TAG, "Session expired. Updating DataStore.")
            clearSession()
        }
    }

    private fun getRemainingSessionTime(): Flow<Long> {
        return dataStore.data.map { preferences ->
            val expirationTime = preferences[SESSION_EXPIRATION_TIME] ?: 0L
            val remainingTimeMs = max(0, expirationTime - System.currentTimeMillis())

            Log.d(
                TAG,
                "Fetching session time: Expiration = ${formatTime(expirationTime)}, Remaining = ${remainingTimeMs / 1000} sec"
            )
            remainingTimeMs
        }
    }

    companion object {
        private val SESSION_EXPIRATION_TIME = longPreferencesKey("session_expiration_time")
        private const val MINUTES_TO_MILLIS = 60 * 1000L
        private const val TAG = "hrishiiii"

        // Date formatter for readable logs
        private val dateFormat = SimpleDateFormat("HH:mm:ss dd/MM/yyyy", Locale.US)

        fun formatTime(timestamp: Long): String {
            return if (timestamp > 0) dateFormat.format(Date(timestamp)) else "N/A"
        }
    }
}