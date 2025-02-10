package com.hrishi.auth.presentation.navigation.model

import kotlinx.serialization.Serializable

@Serializable
data class CreatePinScreenData(
    val username: String,
    val pin: String? = null
)