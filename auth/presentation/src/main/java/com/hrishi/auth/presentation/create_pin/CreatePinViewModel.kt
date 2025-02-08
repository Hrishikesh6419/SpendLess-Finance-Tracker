package com.hrishi.auth.presentation.create_pin

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow

class CreatePinViewModel: ViewModel() {

    private val _uiState = MutableStateFlow(CreatePinState())
    val uiState = _uiState.asStateFlow()

    private val eventChannel = Channel<CreatePinEvent>()
    val events = eventChannel.receiveAsFlow()

    fun onAction(action: CreatePinAction) {
        when(action) {
            is CreatePinAction.OnPinUpdate -> Unit
        }
    }
}