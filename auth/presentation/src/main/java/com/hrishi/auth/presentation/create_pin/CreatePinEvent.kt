package com.hrishi.auth.presentation.create_pin

sealed interface CreatePinEvent {
    data class NavigateToConfirmPinScreen(val createdPin: String) : CreatePinEvent
    data object NavigateToRegisterScreen : CreatePinEvent
    data object NavigateToPreferencesScreen : CreatePinEvent
    data object PinsDoNotMatch : CreatePinEvent
}