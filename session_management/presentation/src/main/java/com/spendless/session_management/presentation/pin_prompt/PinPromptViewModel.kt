package com.spendless.session_management.presentation.pin_prompt

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hrishi.core.domain.auth.usecases.UserInfoUseCases
import com.hrishi.core.domain.preference.usecase.SettingsPreferenceUseCase
import com.hrishi.core.domain.utils.Result
import com.hrishi.presentation.ui.MAX_PIN_LENGTH
import com.spendless.session_management.domain.usecases.SessionUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PinPromptViewModel(
    private val sessionUseCase: SessionUseCase,
    private val settingsPreferenceUseCase: SettingsPreferenceUseCase,
    private val userInfoUseCases: UserInfoUseCases
) : ViewModel() {

    private val _uiState = MutableStateFlow(PinPromptState())
    val uiState = _uiState.asStateFlow()

    private val eventChannel = Channel<PinPromptEvent> {  }
    val events = eventChannel.receiveAsFlow()

    init {
        sessionUseCase.getSessionDataUseCase()
            .flatMapLatest { sessionData ->
                _uiState.update {
                    it.copy(username = sessionData.userName)
                }
                settingsPreferenceUseCase.getPreferencesUseCase(sessionData.userId)
            }
            .onEach { preferences ->
                when (preferences) {
                    is Result.Error -> Unit
                    is Result.Success -> {
                        _uiState.update {
                            it.copy(
                                lockoutDuration = preferences.data.lockOutDuration,
                                remainingPinAttempts = preferences.data.allowedPinAttempts.getValue(),
                                isBiometricsEnabled = preferences.data.isBiometricEnabled
                            )
                        }
                    }
                }
            }
            .launchIn(viewModelScope)
    }

    fun onAction(action: PinPromptAction) {
        viewModelScope.launch {
            when(action) {
                PinPromptAction.OnDeletePressed -> {
                    val pin = _uiState.value.pin
                    _uiState.update {
                        it.copy(
                            pin = pin.dropLast(1)
                        )
                    }
                }

                is PinPromptAction.OnNumberPressed -> {
                    val pin = _uiState.value.pin
                    if (pin.length < MAX_PIN_LENGTH) {
                        _uiState.update {
                            it.copy(
                                pin = pin + action.number
                            )
                        }
                    }
                    val updatedPin = _uiState.value.pin
                    if (updatedPin.length == MAX_PIN_LENGTH) {
                        val userPin = withContext(Dispatchers.IO) {
                            when (val userResult =
                                userInfoUseCases.getUserInfoUseCase(_uiState.value.username)) {
                                is Result.Error -> null
                                is Result.Success -> userResult.data.pin
                            }
                        }

                        if (userPin == updatedPin) {
                            sessionUseCase.resetSessionExpiryUseCase()
                            eventChannel.send(PinPromptEvent.OnSuccessPopBack)
                            return@launch
                        }


                        _uiState.update {
                            it.copy(
                                pin = "",
                                remainingPinAttempts = _uiState.value.remainingPinAttempts - 1
                            )
                        }

                        if (_uiState.value.remainingPinAttempts <= 0) {
                            if (!_uiState.value.isExceededFailedAttempts) {
                                startLockoutTimer()
                            }
                        }

                        eventChannel.send(PinPromptEvent.IncorrectPin)
                    }
                }

                PinPromptAction.OnLogoutClicked -> {
                    sessionUseCase.clearSessionUseCase()
                    eventChannel.send(PinPromptEvent.OnLogout)
                }
            }

        }
    }

    private fun startLockoutTimer() {
        viewModelScope.launch {
            val lockoutDuration = _uiState.value.lockoutDuration.getValueInLong()
            (lockoutDuration downTo 0).asFlow()
                .onEach { secondsLeft ->
                    _uiState.update {
                        it.copy(
                            pin = "",
                            lockoutTimeRemaining = secondsLeft,
                            isExceededFailedAttempts = true
                        )
                    }
                    delay(1000L)
                }
                .onCompletion {
                    _uiState.update {
                        it.copy(
                            isExceededFailedAttempts = false,
                            remainingPinAttempts = 3,
                            lockoutTimeRemaining = 0
                        )
                    }
                }
                .launchIn(viewModelScope)
        }
    }
}