package com.hrishi.core.domain.security

interface EncryptionService {
    fun encrypt(data: String): Pair<String, String>
    fun decrypt(encryptedData: String, iv: String): String
}