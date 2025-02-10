package com.hrishi.auth.presentation.create_pin

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hrishi.auth.presentation.navigation.model.CreatePinScreenData
import com.hrishi.auth.presentation.navigation.model.PreferencesScreenData
import com.hrishi.presentation.ui.getRouteData
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CreatePinViewModel(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(CreatePinState())
    val uiState = _uiState.asStateFlow()

    private val eventChannel = Channel<CreatePinEvent>()
    val events = eventChannel.receiveAsFlow()

    private var createPinScreenData = savedStateHandle.getRouteData<CreatePinScreenData>("screenData")

    fun onAction(action: CreatePinAction) {
        viewModelScope.launch {
            when (action) {
                is CreatePinAction.OnPinUpdate -> Unit
                CreatePinAction.OnDeletePressed -> {
                    val pin = _uiState.value.pin
                    _uiState.update {
                        it.copy(
                            pin = pin.dropLast(1)
                        )
                    }
                }

                is CreatePinAction.OnNumberPressed -> {
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
                        if (createPinScreenData?.pin == null) {
                            eventChannel.send(
                                CreatePinEvent.NavigateToConfirmPinScreen(
                                    CreatePinScreenData(
                                        username = createPinScreenData?.username ?: "",
                                        pin = updatedPin
                                    )
                                )
                            )
                        } else if (updatedPin.equals(createPinScreenData?.pin, true)) {
                            createPinScreenData?.let {
                                eventChannel.send(
                                    CreatePinEvent.NavigateToPreferencesScreen(
                                        screenData = PreferencesScreenData(
                                            it.username,
                                            it.pin
                                        )
                                    )
                                )
                            }
                            resetState()
                        } else {
                            eventChannel.send(CreatePinEvent.PinsDoNotMatch)
                        }
                    }
                }

                CreatePinAction.OnBackPressed -> {
                    eventChannel.send(CreatePinEvent.NavigateToRegisterScreen)
                }
            }
        }
    }

    private fun resetState() {
        _uiState.update {
            it.copy(
                pin = ""
            )
        }
        createPinScreenData = createPinScreenData?.copy(
            pin = ""
        )
    }

    companion object {
        private const val MAX_PIN_LENGTH = 5
    }
}