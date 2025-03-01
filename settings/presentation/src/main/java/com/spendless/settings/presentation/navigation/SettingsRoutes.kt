package com.spendless.settings.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavOptionsBuilder
import kotlinx.serialization.Serializable

@Serializable
data object SettingsBaseRoute

@Serializable
data object SettingsHomeScreenRoute

fun NavController.navigateToSettingsHomeScreen(
    navOptions: NavOptionsBuilder.() -> Unit = {}
) = navigate(SettingsHomeScreenRoute, navOptions)