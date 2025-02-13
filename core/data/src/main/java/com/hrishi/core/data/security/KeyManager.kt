package com.hrishi.core.data.security

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import java.security.KeyStore
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey

object KeyManager {

    // Android Keystore is a secure system that prevents keys from being extracted.
    private const val ANDROID_KEYSTORE = "AndroidKeyStore"

    // A unique alias to identify the key in the Keystore.
    private const val KEY_ALIAS = "SpendLessEncryptionKey"

    fun getOrCreateSecretKey(): SecretKey {
        // Load the Android Keystore. This allows us to access stored keys securely.
        val keyStore = KeyStore.getInstance(ANDROID_KEYSTORE).apply {
            load(null) // The Keystore needs to be loaded before use.
        }

        // Check if the key already exists in the Keystore.
        val entry = keyStore.getEntry(KEY_ALIAS, null) as? KeyStore.SecretKeyEntry
        if (entry != null) {
            // If a key is found, return it to maintain consistency across encryptions.
            return entry.secretKey
        }

        // If no existing key is found, generate a new one.
        return generateSecretKey()
    }

    private fun generateSecretKey(): SecretKey {
        // Get an instance of KeyGenerator to create an AES key inside the Keystore.
        val keyGenerator =
            KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, ANDROID_KEYSTORE)

        // Define the key's properties using KeyGenParameterSpec.
        val keyGenSpec = KeyGenParameterSpec.Builder(
            KEY_ALIAS,
            KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT // The key is used for both encryption and decryption.
        )
            .setBlockModes(KeyProperties.BLOCK_MODE_CBC) // CBC mode requires an IV.
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7) // PKCS7 padding ensures block alignment.
            .setKeySize(256) // AES-256 encryption for strong security.
            //.setIsStrongBoxBacked(true) // StrongBox ensures the key is hardware-backed (API 28+), but is commented out for older devices.
            .build()

        // Initialize the key generator with our specified configuration.
        keyGenerator.init(keyGenSpec)

        // Generate and return the new key. It will be stored in the Keystore automatically.
        return keyGenerator.generateKey()
    }
}
