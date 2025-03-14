package com.hrishi.auth.presentation.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.hrishi.auth.presentation.create_pin.ConfirmPinScreenRoot
import com.hrishi.auth.presentation.create_pin.CreatePinScreenRoot
import com.hrishi.auth.presentation.login.LoginScreenRoot
import com.hrishi.auth.presentation.register.RegisterScreenRoot
import com.hrishi.auth.presentation.user_preference.OnboardingPreferencesScreenRoot
import com.hrishi.presentation.ui.SerializableNavType
import com.hrishi.presentation.ui.navigation.AuthBaseRoute
import com.hrishi.presentation.ui.navigation.ConfirmPinRoute
import com.hrishi.presentation.ui.navigation.CreatePinRoute
import com.hrishi.presentation.ui.navigation.CreatePinScreenData
import com.hrishi.presentation.ui.navigation.LoginRoute
import com.hrishi.presentation.ui.navigation.PreferencesRoute
import com.hrishi.presentation.ui.navigation.PreferencesScreenData
import com.hrishi.presentation.ui.navigation.RegisterRoute
import com.hrishi.presentation.ui.navigation.navigateToConfirmPinScreen
import com.hrishi.presentation.ui.navigation.navigateToCreatePinScreen
import com.hrishi.presentation.ui.navigation.navigateToLoginRoute
import com.hrishi.presentation.ui.navigation.navigateToPreferencesScreen
import com.hrishi.presentation.ui.navigation.navigateToRegisterScreen
import kotlinx.serialization.serializer
import kotlin.reflect.typeOf

fun NavGraphBuilder.authGraph(
    navController: NavHostController,
    onNavigateToDashboardScreen: () -> Unit
) {
    navigation<AuthBaseRoute>(
        startDestination = LoginRoute
    ) {
        composable<LoginRoute> {
            LoginScreenRoot(
                onRegisterClick = {
                    navController.navigateToRegisterScreen {
                        popUpTo<LoginRoute> {
                            inclusive = true
                            saveState = true
                        }
                        restoreState = true
                    }
                },
                onNavigateToDashboard = onNavigateToDashboardScreen
            )
        }

        composable<RegisterRoute> {
            RegisterScreenRoot(
                onAlreadyHaveAnAccountClick = {
                    navController.navigateToLoginRoute {
                        popUpTo<RegisterRoute> {
                            inclusive = true
                            saveState = true
                        }
                        restoreState = true
                    }
                },
                onNavigateToPinScreen = {
                    navController.navigateToCreatePinScreen(screenData = it) {
                        restoreState = false
                    }
                }
            )
        }

        composable<CreatePinRoute>(
            typeMap = mapOf(
                typeOf<CreatePinScreenData>() to SerializableNavType.create(serializer<CreatePinScreenData>())
            )
        ) {
            CreatePinScreenRoot(
                onNavigateToConfirmScreen = {
                    navController.navigateToConfirmPinScreen(screenData = it) {
                        popUpTo<CreatePinRoute> {
                            inclusive = true
                        }
                    }
                },
                onNavigateToRegisterScreen = {
                    navController.navigateToRegisterScreen {
                        popUpTo<AuthBaseRoute> {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable<ConfirmPinRoute>(
            typeMap = mapOf(
                typeOf<CreatePinScreenData>() to SerializableNavType.create(serializer<CreatePinScreenData>())
            )
        ) {
            ConfirmPinScreenRoot(
                onNavigateToRegisterScreen = {
                    navController.navigateToRegisterScreen {
                        popUpTo<AuthBaseRoute> {
                            inclusive = true
                        }
                    }
                },
                onNavigateToPreferencesScreen = {
                    navController.navigateToPreferencesScreen(it)
                }
            )
        }

        composable<PreferencesRoute>(
            typeMap = mapOf(
                typeOf<PreferencesScreenData>() to SerializableNavType.create(serializer<PreferencesScreenData>())
            )
        ) {
            OnboardingPreferencesScreenRoot(
                onNavigateToRegisterScreen = {
                    navController.navigateToRegisterScreen {
                        popUpTo<RegisterRoute>()
                    }
                },
                onNavigateToDashboardScreen = onNavigateToDashboardScreen
            )
        }
    }
}