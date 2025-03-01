package com.spendless.settings.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavOptionsBuilder
import kotlinx.serialization.Serializable

@Serializable
data object SettingsBaseRoute

@Serializable
data object SettingsHomeScreenRoute

@Serializable
data object SettingsPreferenceScreenRoute

@Serializable
data object SettingsSecurityScreenRoute

fun NavController.navigateToSettingsHomeScreen(
    navOptions: NavOptionsBuilder.() -> Unit = {}
) = navigate(SettingsHomeScreenRoute, navOptions)

fun NavController.navigateToSettingsPreferenceScreen(
    navOptions: NavOptionsBuilder.() -> Unit = {}
) = navigate(SettingsPreferenceScreenRoute, navOptions)

fun NavController.navigateToSettingsSecurityScreen(
    navOptions: NavOptionsBuilder.() -> Unit = {}
) = navigate(SettingsSecurityScreenRoute, navOptions)