package com.hrishi.spendless

import com.hrishi.presentation.ui.AppNavRoute

data class MainState(
    val isCheckingAuth: Boolean = false,
    val isSessionExpired: Boolean = false,
    val isUserLoggedIn: Boolean = false,
    val showPinPrompt: Boolean = false,
    val pendingRoute: AppNavRoute? = null,
    val pendingActionAfterAuth: (() -> Unit)? = null
)