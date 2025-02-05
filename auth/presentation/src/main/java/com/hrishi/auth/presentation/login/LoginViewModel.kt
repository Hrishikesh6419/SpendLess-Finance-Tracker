package com.hrishi.auth.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(LoginViewState())
    val uiState = _uiState.asStateFlow()

    private val eventChannel = Channel<LoginEvent>()
    val events = eventChannel.receiveAsFlow()

    fun onAction(action: LoginAction) {
        viewModelScope.launch {
            when (action) {
                LoginAction.OnLoginClick -> {
                    val username = _uiState.value.username.trim()
                    val pin = _uiState.value.pin.trim()

                    if (!isValidUsername(username) || pin.length < 5) {
                        eventChannel.send(LoginEvent.IncorrectCredentials)
                    } else {
                        // Proceed with login logic
                    }
                }

                LoginAction.OnRegisterClick -> {
                    // Handle registration logic
                }

                is LoginAction.OnPinChange -> {
                    _uiState.update {
                        it.copy(pin = action.pin)
                    }
                }

                is LoginAction.OnUsernameUpdate -> {
                    _uiState.update {
                        it.copy(username = action.username)
                    }
                }
            }
        }
    }

    private fun isValidUsername(username: CharSequence): Boolean {
        return username.length in MIN_USERNAME_LENGTH..MAX_USERNAME_LENGTH &&
                username.all { it.isLetterOrDigit() }
    }

    companion object {
        private const val MIN_USERNAME_LENGTH = 3
        private const val MAX_USERNAME_LENGTH = 14
        private const val MIN_PIN_LENGTH = 5
    }
}