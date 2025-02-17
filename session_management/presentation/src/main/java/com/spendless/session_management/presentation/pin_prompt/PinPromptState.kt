package com.spendless.session_management.presentation.pin_prompt

data class PinPromptState(
    val pin: String = "",
    val isTooManyFailedAttempts: Boolean = false,
    val retryCountdownInSeconds: Int = 0
)