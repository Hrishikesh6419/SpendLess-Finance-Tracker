package com.hrishi.spendless.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.hrishi.auth.presentation.navigation.authGraph
import com.hrishi.presentation.ui.NavigationRequestHandler
import com.hrishi.presentation.ui.navigation.AuthBaseRoute
import com.hrishi.presentation.ui.navigation.navigateToDashboardScreen
import com.hrishi.presentation.ui.navigation.navigateToSettingsHomeScreen
import com.spendless.dashboard.presentation.navigation.dashboardNavGraph
import com.spendless.session_management.presentation.navigation.sessionNavGraph
import com.spendless.settings.presentation.navigation.settingsNavGraph

@Composable
fun NavigationRoot(
    navController: NavHostController,
    navigationRequestHandler: NavigationRequestHandler,
    onSessionVerified: () -> Unit = {},
    onLogout: () -> Unit = {},
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = AuthBaseRoute,
        modifier = modifier
    ) {
        authGraph(
            navController = navController,
            onNavigateToDashboardScreen = {
                navController.navigateToDashboardScreen {
                    popUpTo<AuthBaseRoute> { inclusive = true }
                }
            }
        )
        dashboardNavGraph(
            navigationRequestHandler = navigationRequestHandler,
            navController = navController,
            onNavigateToSettings = {
                navController.navigateToSettingsHomeScreen()
            }
        )
        sessionNavGraph(
            navController = navController,
            onVerificationSuccess = {
                onSessionVerified()
                navController.popBackStack()
            },
            onLogout = {
                onLogout()
            }
        )
        settingsNavGraph(
            navController = navController,
            onLogout = {
                onLogout()
            }
        )
    }
}

