package com.hrishi.spendless

import com.hrishi.presentation.ui.AppNavRoute

data class MainState(
    val isCheckingAuth: Boolean = true,
    val authNavigationDestination: AuthNavigationDestination = AuthNavigationDestination.None,
    val isSessionExpired: Boolean = false,
    val isUserLoggedIn: Boolean = false,
    val showPinPrompt: Boolean = false,
    val pendingRoute: AppNavRoute? = null,
    val pendingActionAfterAuth: (() -> Unit)? = null
)

sealed class AuthNavigationDestination {
    data object None: AuthNavigationDestination()
    data object LoginScreen: AuthNavigationDestination()
    data object RegisterScreen: AuthNavigationDestination()
    data object PinScreen: AuthNavigationDestination()
    data class DashboardScreen(val isLaunchedFromWidget: Boolean): AuthNavigationDestination()
}