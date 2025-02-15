package com.hrishi.spendless.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.hrishi.auth.presentation.navigation.AuthBaseRoute
import com.hrishi.auth.presentation.navigation.authGraph
import com.spendless.dashboard.presentation.navigation.dashboardNavGraph
import com.spendless.dashboard.presentation.navigation.navigateToDashboardScreen

@Composable
fun NavigationRoot(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = AuthBaseRoute,
        modifier = modifier,
    ) {
        authGraph(navController = navController,
            onNavigateToDashboardScreen = {
                navController.navigateToDashboardScreen {
                    popUpTo<AuthBaseRoute> {
                        inclusive = true
                    }
                }
            }
        )
        dashboardNavGraph(navController = navController)
    }
}