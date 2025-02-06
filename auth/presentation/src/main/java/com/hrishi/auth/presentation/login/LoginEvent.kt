package com.hrishi.auth.presentation.login

sealed interface LoginEvent {
    data object IncorrectCredentials : LoginEvent
    data object NavigateToRegisterScreen : LoginEvent
}