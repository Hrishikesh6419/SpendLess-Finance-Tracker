package com.hrishi.auth.presentation.create_pin

import com.hrishi.auth.presentation.navigation.model.CreatePinData

sealed interface CreatePinEvent {
    data class NavigateToConfirmPinScreen(val screenData: CreatePinData) : CreatePinEvent
    data object NavigateToRegisterScreen : CreatePinEvent
    data object NavigateToPreferencesScreen : CreatePinEvent
    data object PinsDoNotMatch : CreatePinEvent
}