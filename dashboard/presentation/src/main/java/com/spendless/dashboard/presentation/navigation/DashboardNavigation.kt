package com.spendless.dashboard.presentation.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.spendless.dashboard.presentation.dashboard.DashboardScreenRoot

fun NavGraphBuilder.dashboardNavGraph(
    navController: NavHostController
) {
    navigation<DashboardBaseRoute>(
        startDestination = DashboardScreenRoute
    ) {
        composable<DashboardScreenRoute> {
            DashboardScreenRoot()
        }
    }
}