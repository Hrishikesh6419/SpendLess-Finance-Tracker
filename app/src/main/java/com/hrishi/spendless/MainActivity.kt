package com.hrishi.spendless

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.hrishi.core.presentation.designsystem.SpendLessFinanceTrackerTheme
import com.hrishi.presentation.ui.navigateToRoute
import com.hrishi.presentation.ui.navigation.AuthBaseRoute
import com.hrishi.presentation.ui.navigation.SessionBaseRoute
import com.hrishi.presentation.ui.navigation.navigateToLoginRoute
import com.hrishi.spendless.navigation.NavigationRoot
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {

    private val mainViewModel by viewModel<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val splashScreen = installSplashScreen()
        splashScreen.setKeepOnScreenCondition {
            mainViewModel.uiState.value.isCheckingAuth
        }

        setContent {
            SpendLessFinanceTrackerTheme {
                val uiState by mainViewModel.uiState.collectAsStateWithLifecycle()
                if (!uiState.isCheckingAuth) {
                    val navController = rememberNavController()

                    LaunchedEffect(uiState.showPinPrompt) {
                        if (uiState.showPinPrompt) {
                            navController.navigate(SessionBaseRoute)
                        }
                    }

                    uiState.pendingRoute?.let { route ->
                        if (!uiState.isSessionExpired && uiState.isUserLoggedIn) {
                            navController.navigateToRoute(route)
                            mainViewModel.mainViewModelClearPendingRoute()
                        }
                    }

                    NavigationRoot(
                        navController = navController,
                        navigationRequestHandler = mainViewModel,
                        onSessionVerified = {
                            mainViewModel.startSession()
                            uiState.pendingActionAfterAuth?.invoke()
                            mainViewModel.onPinVerified()
                            mainViewModel.clearPendingActionAfterAuth()
                        },
                        onLogout = {
                            navController.navigateToLoginRoute {
                                popUpTo(navController.graph.id) {
                                    inclusive = true
                                }
                            }
                        },
                        authNavigationDestination = uiState.authNavigationDestination
                    )
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        mainViewModel.onAppResumed()
    }

    override fun onDestroy() {
        super.onDestroy()
        mainViewModel.setSessionToExpired()
    }
}