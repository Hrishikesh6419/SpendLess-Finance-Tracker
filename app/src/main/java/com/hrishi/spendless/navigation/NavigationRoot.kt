package com.hrishi.spendless.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.hrishi.auth.presentation.navigation.AuthBaseRoute
import com.hrishi.auth.presentation.navigation.authGraph
import com.hrishi.spendless.MainViewModel
import com.spendless.dashboard.presentation.navigation.dashboardNavGraph
import com.spendless.dashboard.presentation.navigation.navigateToDashboardScreen
import com.spendless.session_management.presentation.navigation.navigateToPinPromptScreen
import com.spendless.session_management.presentation.navigation.sessionNavGraph
import org.koin.androidx.compose.koinViewModel

@Composable
fun NavigationRoot(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    mainViewModel: MainViewModel = koinViewModel()
) {
    val state by mainViewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(state.isSessionExpired) {
        if (state.isSessionExpired) {
            navController.navigateToPinPromptScreen {
                launchSingleTop = true
            }
            mainViewModel.updateExpiry(isExpired = false)
        }
    }

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
        sessionNavGraph(navController = navController,
            onVerificationSuccess = {
                mainViewModel.startSession()
                navController.popBackStack()
            })
    }
}