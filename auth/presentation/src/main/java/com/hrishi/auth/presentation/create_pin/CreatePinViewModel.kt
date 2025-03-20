package com.hrishi.auth.presentation.create_pin

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hrishi.auth.domain.usecase.CreatePinUseCases
import com.hrishi.presentation.ui.MAX_PIN_LENGTH
import com.hrishi.presentation.ui.getRouteData
import com.hrishi.presentation.ui.navigation.CreatePinScreenData
import com.hrishi.presentation.ui.navigation.PreferencesScreenData
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CreatePinViewModel(
    savedStateHandle: SavedStateHandle,
    private val createPinUseCases: CreatePinUseCases
) : ViewModel() {

    private val _uiState = MutableStateFlow(CreatePinState())
    val uiState = _uiState.asStateFlow()

    private val eventChannel = Channel<CreatePinEvent>()
    val events = eventChannel.receiveAsFlow()

    private val createPinScreenData = savedStateHandle.getRouteData<CreatePinScreenData>("screenData")

    fun onAction(action: CreatePinAction) {
        when (action) {
            CreatePinAction.OnDeletePressed -> deleteDigit()
            is CreatePinAction.OnNumberPressed -> appendDigit(action.number.toString())
            CreatePinAction.OnBackPressed -> emitEvent(CreatePinEvent.OnBackClick)
        }
    }

    private fun appendDigit(digit: String) {
        viewModelScope.launch {
            val updatedPin = createPinUseCases.appendDigitUseCase(_uiState.value.pin, digit)
            _uiState.update { it.copy(pin = updatedPin) }

            if (updatedPin.length == MAX_PIN_LENGTH) {
                processFullPin(updatedPin)
            }
        }
    }

    private fun deleteDigit() {
        val updatedPin = createPinUseCases.deleteDigitUseCase(_uiState.value.pin)
        _uiState.update { it.copy(pin = updatedPin) }
    }

    private fun processFullPin(updatedPin: String) {
        when {
            createPinScreenData?.pin.isNullOrEmpty() -> {
                resetPin()
                emitEvent(
                    CreatePinEvent.NavigateToConfirmPinScreen(
                        CreatePinScreenData(
                            username = createPinScreenData?.username.orEmpty(),
                            pin = updatedPin
                        )
                    )
                )
            }

            createPinUseCases.validatePinMatchUseCase(updatedPin, createPinScreenData?.pin) -> {
                emitEvent(
                    CreatePinEvent.NavigateToPreferencesScreen(
                        PreferencesScreenData(
                            username = createPinScreenData?.username.orEmpty(),
                            pin = updatedPin
                        )
                    )
                )
                resetPin()
            }

            else -> {
                resetPin()
                emitEvent(CreatePinEvent.PinsDoNotMatch)
            }
        }
    }

    private fun resetPin() {
        _uiState.update { it.copy(pin = "") }
    }

    private fun emitEvent(event: CreatePinEvent) {
        viewModelScope.launch { eventChannel.send(event) }
    }
}