package com.hrishi.auth.presentation.create_pin

import com.hrishi.auth.presentation.navigation.model.CreatePinScreenData
import com.hrishi.auth.presentation.navigation.model.PreferencesScreenData

sealed interface CreatePinEvent {
    data class NavigateToConfirmPinScreen(val screenData: CreatePinScreenData) : CreatePinEvent
    data object NavigateToRegisterScreen : CreatePinEvent
    data class NavigateToPreferencesScreen(val screenData: PreferencesScreenData) : CreatePinEvent
    data object PinsDoNotMatch : CreatePinEvent
}