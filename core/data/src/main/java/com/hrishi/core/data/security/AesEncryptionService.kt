package com.hrishi.core.data.security

import android.util.Base64
import com.hrishi.core.domain.security.EncryptionService
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec

// This class provides AES encryption and decryption services using a securely stored key.
class AesEncryptionService(private val secretKey: SecretKey) : EncryptionService {

    // The transformation specifies the encryption algorithm, mode, and padding scheme.
    // We use AES in Cipher Block Chaining (CBC) mode with PKCS7 padding.
    private val transformation = "AES/CBC/PKCS7Padding"

    override fun encrypt(data: String): String {
        // Get an instance of the Cipher configured for our transformation.
        val cipher = Cipher.getInstance(transformation)

        // Initialize the cipher in ENCRYPT_MODE using the provided secret key.
        // This step generates a new Initialization Vector (IV) for added security.
        cipher.init(Cipher.ENCRYPT_MODE, secretKey)

        // Extract the IV, which we will need later for decryption.
        val iv = cipher.iv

        // Encrypt the input data (converted to bytes) and store the encrypted output.
        val encryptedBytes = cipher.doFinal(data.toByteArray())

        // Convert the encrypted bytes and IV to Base64 for easier storage and transmission.
        return "${encryptedBytes.toBase64()}:${iv.toBase64()}"
    }

    override fun decrypt(encryptedData: String): String {
        val parts = encryptedData.split(":")
        if (parts.size != 2) return ""

        val encryptedDataPart1 = parts[0].fromBase64()
        val iv = parts[1].fromBase64()

        // Get a Cipher instance again, using the same transformation as before.
        val cipher = Cipher.getInstance(transformation)

        // Initialize the cipher in DECRYPT_MODE, passing the secret key and the original IV.
        // Using the same IV ensures we get the correct decrypted output.
        cipher.init(Cipher.DECRYPT_MODE, secretKey, IvParameterSpec(iv))

        // Decrypt the data (converting it back from Base64) to retrieve the original input.
        val decryptedBytes = cipher.doFinal(encryptedDataPart1)

        // Convert the decrypted bytes back into a string and return it.
        return String(decryptedBytes)
    }

    // Helper function to encode byte arrays into a Base64 string for storage.
    private fun ByteArray.toBase64(): String = Base64.encodeToString(this, Base64.DEFAULT)

    // Helper function to decode a Base64 string back into a byte array.
    private fun String.fromBase64(): ByteArray = Base64.decode(this, Base64.DEFAULT)
}
