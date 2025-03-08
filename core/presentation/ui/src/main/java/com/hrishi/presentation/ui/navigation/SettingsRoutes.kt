package com.hrishi.presentation.ui.navigation

import androidx.navigation.NavController
import androidx.navigation.NavOptionsBuilder
import kotlinx.serialization.Serializable

@Serializable
data object SettingsBaseRoute : AppRoute

@Serializable
data object SettingsHomeScreenRoute : AppRoute

@Serializable
data object SettingsPreferenceScreenRoute : AppRoute

@Serializable
data object SettingsSecurityScreenRoute : AppRoute

fun NavController.navigateToSettingsHomeScreen(
    navOptions: NavOptionsBuilder.() -> Unit = {}
) = navigate(SettingsHomeScreenRoute, navOptions)

fun NavController.navigateToSettingsPreferenceScreen(
    navOptions: NavOptionsBuilder.() -> Unit = {}
) = navigate(SettingsPreferenceScreenRoute, navOptions)

fun NavController.navigateToSettingsSecurityScreen(
    navOptions: NavOptionsBuilder.() -> Unit = {}
) = navigate(SettingsSecurityScreenRoute, navOptions)