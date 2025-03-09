package com.hrishi.presentation.ui.navigation

import androidx.navigation.NavController
import androidx.navigation.NavOptionsBuilder
import kotlinx.serialization.Serializable

@Serializable
data object DashboardBaseRoute : AppRoute

@Serializable
data class DashboardScreenRoute(val isLaunchedFromWidget: Boolean = false) : AppRoute

@Serializable
data object AllTransactionsScreenRoute : AppRoute

fun NavController.navigateToDashboardScreen(
    navOptions: NavOptionsBuilder.() -> Unit = {}
) = navigate(DashboardScreenRoute(), navOptions)

fun NavController.navigateToAllTransactionsScreenRoute(
    navOptions: NavOptionsBuilder.() -> Unit = {}
) = navigate(AllTransactionsScreenRoute, navOptions)