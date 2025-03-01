package com.spendless.settings.presentation.security

import com.hrishi.core.domain.model.BiometricPromptStatus
import com.hrishi.core.domain.model.LockoutDuration
import com.hrishi.core.domain.model.SessionDuration

data class SettingsSecurityViewState(
    val biometricPromptStatus: BiometricPromptStatus,
    val sessionExpiryDuration: SessionDuration,
    val lockedOutDuration: LockoutDuration,
    val enableSaveButton: Boolean = true
)