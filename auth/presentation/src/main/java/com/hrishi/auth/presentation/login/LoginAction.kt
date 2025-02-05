package com.hrishi.auth.presentation.login

sealed interface LoginAction {
    data object OnLoginClick : LoginAction
    data object OnRegisterClick : LoginAction
    data class OnUsernameUpdate(val username: String) : LoginAction
    data class OnPinChange(val pin: String) : LoginAction
}