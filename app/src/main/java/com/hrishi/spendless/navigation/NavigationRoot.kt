package com.hrishi.spendless.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.hrishi.auth.presentation.navigation.AuthBaseRoute
import com.hrishi.auth.presentation.navigation.authGraph
import com.hrishi.auth.presentation.navigation.navigateToLoginRoute
import com.hrishi.spendless.MainViewModel
import com.spendless.dashboard.presentation.navigation.DashboardBaseRoute
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

    // TODO: Start - Clean up and improve pin navigation
    var startDestination: Any? by remember { mutableStateOf(null) }
    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(Unit) {
        if (mainViewModel.isUserIdPresent()) {
            startDestination = DashboardBaseRoute
        }
    }

    LaunchedEffect(lifecycleOwner) {
        lifecycleOwner.lifecycle.addObserver(object : DefaultLifecycleObserver {
            override fun onStop(owner: LifecycleOwner) {
                mainViewModel.setSessionToExpired()
            }

            override fun onDestroy(owner: LifecycleOwner) {
                super.onDestroy(owner)
                mainViewModel.setSessionToExpired()
            }
        })
    }

    if (startDestination == null) {
        return
    }

    // TODO: End - Clean up and improve pin navigation

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
        startDestination = startDestination ?: AuthBaseRoute,
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
            },
            onLogout = {
                navController.navigateToLoginRoute {
                    popUpTo<AuthBaseRoute>()
                }
            }
        )
    }
}