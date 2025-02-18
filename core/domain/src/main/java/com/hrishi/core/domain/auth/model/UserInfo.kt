package com.hrishi.core.domain.auth.model

data class UserInfo(
    val userId: Long? = null,
    val username: String,
    val encryptedPin: String,
    val iv: String,
)

data class UserInfoDecrypted(
    val userId: Long? = null,
    val username: String,
    val pin: String
)