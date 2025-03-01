package com.spendless.settings.presentation.preference

sealed interface SettingsPreferencesEvent {
    data object NavigateBack : SettingsPreferencesEvent
    data object PreferencesSaved : SettingsPreferencesEvent
}