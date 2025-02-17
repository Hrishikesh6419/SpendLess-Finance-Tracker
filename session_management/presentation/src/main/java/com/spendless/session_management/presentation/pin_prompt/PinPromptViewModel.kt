package com.spendless.session_management.presentation.pin_prompt

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hrishi.presentation.ui.MAX_PIN_LENGTH
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PinPromptViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(PinPromptState())
    val uiState = _uiState.asStateFlow()

    private val eventChannel = Channel<PinPromptEvent> {  }
    val events = eventChannel.receiveAsFlow()

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
                        // TODO: Update this
                        eventChannel.send(PinPromptEvent.OnSuccessPopBack)
                    }
                }

                PinPromptAction.OnLogoutClicked -> {

                }
            }

        }
    }
}