package com.hrishi.auth.presentation.login

import androidx.compose.foundation.text.input.TextFieldState

data class LoginViewState(
    val username: TextFieldState = TextFieldState(),
    val pin: TextFieldState = TextFieldState()
)