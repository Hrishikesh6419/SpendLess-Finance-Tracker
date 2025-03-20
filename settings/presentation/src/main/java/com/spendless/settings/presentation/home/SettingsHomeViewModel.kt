package com.spendless.settings.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spendless.session_management.domain.usecases.SessionUseCases
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class SettingsHomeViewModel(
    private val sessionUseCases: SessionUseCases
) : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsHomeViewState())
    val uiState = _uiState.asStateFlow()

    private val eventChannel = Channel<SettingsHomeEvent>()
    val events = eventChannel.receiveAsFlow()

    fun onAction(action: SettingsHomeAction) {
        viewModelScope.launch {
            when (action) {
                SettingsHomeAction.OnLogoutClick -> {
                    sessionUseCases.clearSessionUseCase()
                    eventChannel.send(SettingsHomeEvent.Logout)
                }

                SettingsHomeAction.OnBackClick -> {
                    eventChannel.send(SettingsHomeEvent.NavigateBack)
                }

                SettingsHomeAction.OnPreferencesClick -> {
                    eventChannel.send(SettingsHomeEvent.NavigateToPreferencesScreen)
                }

                SettingsHomeAction.OnSecurityClick -> {
                    eventChannel.send(SettingsHomeEvent.NavigateToSecurityScreen)
                }
            }
        }
    }
}