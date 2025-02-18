package com.spendless.session_management.presentation.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.spendless.session_management.presentation.pin_prompt.PinPromptScreenRoot

fun NavGraphBuilder.sessionNavGraph(
    navController: NavHostController,
    onVerificationSuccess: () -> Unit,
    onLogout: () -> Unit
) {
    navigation<SessionBaseRoute>(
        startDestination = PinPromptScreenRoute
    ) {
        composable<PinPromptScreenRoute> {
            PinPromptScreenRoot(
                onSuccessClick = onVerificationSuccess,
                onLogout = onLogout
            )
        }
    }
}