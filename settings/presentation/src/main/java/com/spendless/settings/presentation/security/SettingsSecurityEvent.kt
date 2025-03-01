package com.spendless.settings.presentation.security

sealed interface SettingsSecurityEvent {
    data object SecuritySettingsSaved : SettingsSecurityEvent
    data object NavigateBack : SettingsSecurityEvent
}