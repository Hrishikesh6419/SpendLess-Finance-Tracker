package com.hrishi.auth.presentation.create_pin

import com.hrishi.presentation.ui.navigation.CreatePinScreenData
import com.hrishi.presentation.ui.navigation.PreferencesScreenData

sealed interface CreatePinEvent {
    data class NavigateToConfirmPinScreen(val screenData: CreatePinScreenData) : CreatePinEvent
    data object OnBackClick : CreatePinEvent
    data class NavigateToPreferencesScreen(val screenData: PreferencesScreenData) : CreatePinEvent
    data object PinsDoNotMatch : CreatePinEvent
}