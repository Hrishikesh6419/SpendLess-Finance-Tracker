package com.hrishi.auth.presentation.login

sealed interface LoginAction {
    data object OnLoginClick : LoginAction
    data object OnRegisterClick : LoginAction
}