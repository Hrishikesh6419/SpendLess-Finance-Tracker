package com.spendless.core.database.security

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import java.util.UUID

object DatabaseSecurity {
    private const val DATABASE_PASSPHRASE = "db_passphrase"
    private const val SECURED_PREFERENCES_FILE_NAME = "secure_prefs"

    fun getDatabasePassphrase(context: Context): String {
        val masterKey = MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()

        val sharedPreferences = EncryptedSharedPreferences.create(
            context,
            SECURED_PREFERENCES_FILE_NAME,
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )

        return sharedPreferences.getString(DATABASE_PASSPHRASE, null) ?: generateAndStorePassphrase(
            sharedPreferences
        )
    }

    private fun generateAndStorePassphrase(sharedPreferences: SharedPreferences): String {
        val passphrase = UUID.randomUUID().toString()
        sharedPreferences.edit().putString(DATABASE_PASSPHRASE, passphrase).apply()
        return passphrase
    }
}