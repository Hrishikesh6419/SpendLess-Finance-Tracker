package com.spendless.settings.presentation.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.hrishi.presentation.ui.navigation.SettingsBaseRoute
import com.hrishi.presentation.ui.navigation.SettingsHomeScreenRoute
import com.hrishi.presentation.ui.navigation.SettingsPreferenceScreenRoute
import com.hrishi.presentation.ui.navigation.SettingsSecurityScreenRoute
import com.hrishi.presentation.ui.navigation.navigateToSettingsPreferenceScreen
import com.hrishi.presentation.ui.navigation.navigateToSettingsSecurityScreen
import com.spendless.settings.presentation.home.SettingsHomeScreenRoot
import com.spendless.settings.presentation.preference.SettingsPreferenceScreenRoot
import com.spendless.settings.presentation.security.SettingsSecurityScreenRoot

fun NavGraphBuilder.settingsNavGraph(
    navController: NavHostController,
    onLogout: () -> Unit
) {
    navigation<SettingsBaseRoute>(
        startDestination = SettingsHomeScreenRoute
    ) {
        composable<SettingsHomeScreenRoute> {
            SettingsHomeScreenRoot(
                onNavigateToPreference = {
                    navController.navigateToSettingsPreferenceScreen()
                },
                onNavigateToSettings = {
                    navController.navigateToSettingsSecurityScreen()
                },
                onLogout = onLogout,
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        composable<SettingsPreferenceScreenRoute> {
            SettingsPreferenceScreenRoot(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        composable<SettingsSecurityScreenRoute> {
            SettingsSecurityScreenRoot(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}