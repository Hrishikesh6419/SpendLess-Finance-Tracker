package com.hrishi.core.domain.auth.model

data class UserInfo(
    val userId: Long? = null,
    val username: String,
    val pin: String
)