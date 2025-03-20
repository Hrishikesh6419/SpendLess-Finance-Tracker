package com.hrishi.auth.presentation.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hrishi.auth.domain.usecase.LoginUseCases
import com.hrishi.auth.domain.usecase.RegisterUseCases
import com.hrishi.presentation.ui.navigation.CreatePinScreenData
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RegisterViewModel(
    private val loginUseCases: LoginUseCases,
    private val registerUseCases: RegisterUseCases,
) : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterViewState())
    val uiState = _uiState.asStateFlow()

    private val eventChannel = Channel<RegisterEvent>()
    val events = eventChannel.receiveAsFlow()

    fun onAction(action: RegisterAction) {
        when (action) {
            is RegisterAction.OnUserNameChanged -> updateUserName(action.username)
            RegisterAction.OnAlreadyHaveAnAccountClicked -> navigateToLoginScreen()
            RegisterAction.OnNextClicked -> handleNextClicked()
        }
    }

    private fun updateUserName(username: String) {
        _uiState.update {
            it.copy(
                username = username,
                isNextEnabled = username.isNotEmpty()
            )
        }
    }

    private fun navigateToLoginScreen() {
        viewModelScope.launch {
            eventChannel.send(RegisterEvent.NavigateToLoginScreen)
        }
    }

    private fun handleNextClicked() {
        viewModelScope.launch {
            val username = _uiState.value.username
            val isValid = loginUseCases.isUsernameValidUseCase(username)
            if (!isValid) {
                eventChannel.send(RegisterEvent.IncorrectUsername)
                return@launch
            }

            val isDuplicate = registerUseCases.isUserNameDuplicateUseCase(username)
            if (isDuplicate) {
                _uiState.update { it.copy(isNextEnabled = false) }
                eventChannel.send(RegisterEvent.DuplicateUsername)
                return@launch
            }

            resetState()
            eventChannel.send(RegisterEvent.NavigateToPinScreen(CreatePinScreenData(username)))
        }
    }

    private fun resetState() {
        _uiState.update {
            it.copy(
                username = "",
                isNextEnabled = false
            )
        }
    }
}