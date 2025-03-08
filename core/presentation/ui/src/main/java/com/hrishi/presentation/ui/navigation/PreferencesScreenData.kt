package com.hrishi.presentation.ui.navigation

import kotlinx.serialization.Serializable

@Serializable
data class PreferencesScreenData(
    val username: String,
    val pin: String? = null
)