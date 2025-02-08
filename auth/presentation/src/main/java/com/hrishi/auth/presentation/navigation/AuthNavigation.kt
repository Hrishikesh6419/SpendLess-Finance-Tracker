package com.hrishi.auth.presentation.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.hrishi.auth.presentation.create_pin.CreatePinScreenRoot
import com.hrishi.auth.presentation.login.LoginScreenRoot
import com.hrishi.auth.presentation.register.RegisterScreenRoot

fun NavGraphBuilder.authGraph(
    navController: NavHostController
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
                }
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
                    navController.navigateToCreatePinScreen {
                        restoreState = false
                    }
                }
            )
        }

        composable<CreatePinRoute> {
            CreatePinScreenRoot()
        }
    }
}