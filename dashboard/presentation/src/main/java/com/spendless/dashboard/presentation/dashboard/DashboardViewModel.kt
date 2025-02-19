package com.spendless.dashboard.presentation.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spendless.session_management.domain.usecases.SessionUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class DashboardViewModel(
    private val sessionUseCases: SessionUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(DashboardState(isSessionExpired = false))
    val uiState = _uiState.asStateFlow()

    private val eventChannel = Channel<DashboardEvent>()
    val events = eventChannel.receiveAsFlow()

    fun triggerPin() {
        viewModelScope.launch {
            if (sessionUseCases.isSessionExpiredUseCase().first()) {
                // TODO: Add Dashboard Logic
            }
        }
    }
}