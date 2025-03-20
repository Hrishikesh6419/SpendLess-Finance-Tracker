package com.hrishi.auth.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hrishi.auth.domain.usecase.LoginUseCases
import com.hrishi.core.domain.utils.Result
import com.spendless.session_management.domain.model.SessionData
import com.spendless.session_management.domain.usecases.SessionUseCases
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginViewModel(
    private val loginUseCases: LoginUseCases,
    private val sessionUseCases: SessionUseCases
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginViewState())
    val uiState = _uiState.asStateFlow()

    private val eventChannel = Channel<LoginEvent>()
    val events = eventChannel.receiveAsFlow()

    fun onAction(action: LoginAction) {
        when (action) {
            LoginAction.OnLoginClick -> handleLogin()

            LoginAction.OnRegisterClick -> {
                viewModelScope.launch {
                    emitEvent(LoginEvent.NavigateToRegisterScreen)
                }
            }

            is LoginAction.OnPinChange -> _uiState.update { it.copy(pin = action.pin) }

            is LoginAction.OnUsernameUpdate -> _uiState.update { it.copy(username = action.username) }
        }
    }

    private fun handleLogin() {
        viewModelScope.launch {
            val username = _uiState.value.username.trim()
            val pin = _uiState.value.pin.trim()

            if (!loginUseCases.isUsernameValidUseCase(username) || pin.length < MIN_PIN_LENGTH) {
                emitEvent(LoginEvent.IncorrectCredentials)
                return@launch
            }

            val loginResult = withContext(Dispatchers.IO) {
                loginUseCases.initiateLoginUseCase(username, pin)
            }

            when (loginResult) {
                is Result.Success -> {
                    if (loginResult.data > 0L) {
                        withContext(Dispatchers.IO) {
                            sessionUseCases.saveSessionUseCase(
                                SessionData(
                                    userId = loginResult.data,
                                    userName = username,
                                    sessionExpiryTime = 0
                                )
                            )
                        }
                        emitEvent(LoginEvent.NavigateToDashboardScreen)
                    } else {
                        emitEvent(LoginEvent.IncorrectCredentials)
                    }
                }

                is Result.Error -> emitEvent(LoginEvent.IncorrectCredentials)
            }
        }
    }

    private suspend fun emitEvent(event: LoginEvent) {
        withContext(Dispatchers.Main) {
            eventChannel.send(event)
        }
    }

    companion object {
        private const val MIN_PIN_LENGTH = 5
    }
}