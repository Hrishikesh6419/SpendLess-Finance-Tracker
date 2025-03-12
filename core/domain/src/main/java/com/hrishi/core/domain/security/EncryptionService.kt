package com.hrishi.core.domain.security

interface EncryptionService {
    fun encrypt(data: String): String
    fun decrypt(encryptedData: String): String
}