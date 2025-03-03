package com.spendless.dashboard.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavOptionsBuilder
import kotlinx.serialization.Serializable

@Serializable
data object DashboardBaseRoute

@Serializable
data object DashboardScreenRoute

@Serializable
data object AllTransactionsScreenRoute

fun NavController.navigateToDashboardScreen(
    navOptions: NavOptionsBuilder.() -> Unit = {}
) = navigate(DashboardScreenRoute, navOptions)

fun NavController.navigateToAllTransactionsScreenRoute(
    navOptions: NavOptionsBuilder.() -> Unit = {}
) = navigate(AllTransactionsScreenRoute, navOptions)