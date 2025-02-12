package com.hrishi.auth.presentation.user_preference

sealed interface OnboardingPreferencesEvent {
    data object NavigateToRegisterScreen : OnboardingPreferencesEvent
    data object NavigateToDashboardScreen : OnboardingPreferencesEvent
}