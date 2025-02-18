package com.spendless.session_management.presentation.pin_prompt

import com.hrishi.core.domain.model.LockoutDuration

data class PinPromptState(
    val username: String = "",
    val pin: String = "",
    val lockoutDuration: LockoutDuration = LockoutDuration.FIFTEEN_SECONDS,
    val remainingPinAttempts: Long = 3,
    val isExceededFailedAttempts: Boolean = false,
    val lockoutTimeRemaining: Long = 0L,
)