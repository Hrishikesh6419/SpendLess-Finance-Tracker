package com.hrishi.spendless.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.hrishi.auth.presentation.navigation.authGraph
import com.hrishi.presentation.ui.AppNavRoute
import com.hrishi.presentation.ui.LocalAuthActionHandler
import com.hrishi.presentation.ui.LocalAuthNavigationHandler
import com.hrishi.presentation.ui.NavigationRequestHandler
import com.hrishi.presentation.ui.navigation.AuthBaseRoute
import com.hrishi.presentation.ui.navigation.DashboardBaseRoute
import com.hrishi.presentation.ui.navigation.SessionBaseRoute
import com.hrishi.presentation.ui.navigation.SettingsHomeScreenRoute
import com.hrishi.presentation.ui.navigation.navigateToDashboardScreen
import com.hrishi.spendless.AuthNavigationDestination
import com.spendless.dashboard.presentation.navigation.dashboardNavGraph
import com.spendless.session_management.presentation.navigation.sessionNavGraph
import com.spendless.settings.presentation.navigation.settingsNavGraph

@Composable
fun NavigationRoot(
    navController: NavHostController,
    navigationRequestHandler: NavigationRequestHandler,
    onSessionVerified: () -> Unit = {},
    onLogout: () -> Unit = {},
    authNavigationDestination: AuthNavigationDestination,
    modifier: Modifier = Modifier,
) {
    CompositionLocalProvider(
        LocalAuthActionHandler provides { onVerified: () -> Unit ->
            navigationRequestHandler.runWithAuthCheck(onVerified)
        },
        LocalAuthNavigationHandler provides { appNavRoute: AppNavRoute ->
            navigationRequestHandler.navigateWithAuthCheck(appNavRoute)
        }
    ) {
        NavHost(
            navController = navController,
            startDestination = getStartDestination(authNavigationDestination),
            modifier = modifier
        ) {
            authGraph(
                navController = navController,
                shouldNavigateToLogin = (authNavigationDestination as? AuthNavigationDestination.AuthScreen)?.shouldNavigateToLogin,
                onNavigateToDashboardScreen = {
                    navController.navigateToDashboardScreen {
                        popUpTo<AuthBaseRoute> { inclusive = true }
                    }
                }
            )
            dashboardNavGraph(
                navController = navController,
                isLaunchedFromWidget = (authNavigationDestination as? AuthNavigationDestination.DashboardScreen)?.isLaunchedFromWidget
                    ?: false,
                onNavigateToSettings = {
                    navigationRequestHandler.navigateWithAuthCheck(
                        AppNavRoute(
                            pendingRoute = SettingsHomeScreenRoute
                        )
                    )
                }
            )
            sessionNavGraph(
                navController = navController,
                onVerificationSuccess = {
                    onSessionVerified()
                    navController.navigateUp()
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
}

private fun getStartDestination(authNavigationDestination: AuthNavigationDestination) =
    when (authNavigationDestination) {
        is AuthNavigationDestination.DashboardScreen -> DashboardBaseRoute
        is AuthNavigationDestination.AuthScreen -> AuthBaseRoute
        AuthNavigationDestination.None -> AuthBaseRoute
        AuthNavigationDestination.PinScreen -> SessionBaseRoute
    }

