package com.hrishi.auth.presentation.register

sealed interface RegisterEvent {
    data object UsernameTaken : RegisterEvent
    data object SuccessfulRegistration : RegisterEvent
    data object NavigateToRegisterScreen : RegisterEvent
    data object IncorrectUsername : RegisterEvent
}