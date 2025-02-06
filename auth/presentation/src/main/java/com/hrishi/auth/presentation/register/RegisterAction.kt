package com.hrishi.auth.presentation.register

sealed interface RegisterAction {
    data object OnAlreadyHaveAnAccountClicked : RegisterAction
    data class OnUserNameChanged(val username: String) : RegisterAction
}