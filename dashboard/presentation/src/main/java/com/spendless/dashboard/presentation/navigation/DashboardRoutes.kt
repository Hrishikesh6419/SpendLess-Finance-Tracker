package com.spendless.dashboard.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavOptionsBuilder
import kotlinx.serialization.Serializable

@Serializable
data object DashboardBaseRoute

@Serializable
data object DashboardScreenRoute

fun NavController.navigateToDashboardScreen(
    navOptions: NavOptionsBuilder.() -> Unit = {}
) = navigate(DashboardScreenRoute, navOptions)