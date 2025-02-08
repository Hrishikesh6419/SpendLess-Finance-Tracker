package com.hrishi.auth.presentation.register

sealed interface RegisterEvent {
    data class EnableNextButton(val isEnabled: Boolean) : RegisterEvent
    data object UsernameTaken : RegisterEvent
    data object SuccessfulRegistration : RegisterEvent
    data object NavigateToRegisterScreen : RegisterEvent
}