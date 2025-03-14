package com.hrishi.auth.presentation.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hrishi.auth.domain.usecase.LoginUseCases
import com.hrishi.auth.domain.usecase.RegisterUseCases
import com.hrishi.presentation.ui.navigation.CreatePinScreenData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RegisterViewModel(
    private val loginUseCases: LoginUseCases,
    private val registerUseCases: RegisterUseCases,
) : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterViewState())
    val uiState = _uiState.asStateFlow()

    private val eventChannel = Channel<RegisterEvent>()
    val events = eventChannel.receiveAsFlow()

    fun onAction(action: RegisterAction) {
        viewModelScope.launch {
            when (action) {
                is RegisterAction.OnUserNameChanged -> {
                    _uiState.update {
                        it.copy(
                            username = action.username,
                            isNextEnabled = action.username.isNotEmpty()
                        )
                    }
                }

                RegisterAction.OnAlreadyHaveAnAccountClicked ->
                    eventChannel.send(RegisterEvent.NavigateToRegisterScreen)

                RegisterAction.OnNextClicked -> {
                    val username = _uiState.value.username
                    if (!loginUseCases.isUsernameValidUseCase(username)) {
                        eventChannel.send(RegisterEvent.IncorrectUsername)
                        return@launch
                    }

                    val isUserNameDuplicate = withContext(Dispatchers.IO) {
                        registerUseCases.isUserNameDuplicateUseCase(username)
                    }

                    if (isUserNameDuplicate) {
                        _uiState.update {
                            it.copy(
                                isNextEnabled = false
                            )
                        }
                        eventChannel.send(RegisterEvent.DuplicateUsername)
                        return@launch
                    }

                    resetState()
                    eventChannel.send(
                        RegisterEvent.NavigateToPinScreen(
                            CreatePinScreenData(username = username)
                        )
                    )
                }
            }
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