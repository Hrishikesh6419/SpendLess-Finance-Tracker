package com.hrishi.presentation.ui.navigation

import androidx.navigation.NavController
import androidx.navigation.NavOptionsBuilder
import kotlinx.serialization.Serializable

// Routes
@Serializable
data object AuthBaseRoute : AppRoute

@Serializable
data object LoginRoute : AppRoute

@Serializable
data object RegisterRoute : AppRoute

@Serializable
data class CreatePinRoute(val screenData: CreatePinScreenData) : AppRoute

@Serializable
data class ConfirmPinRoute(val screenData: CreatePinScreenData) : AppRoute

@Serializable
data class PreferencesRoute(val screenData: PreferencesScreenData) : AppRoute

// Navigation Extensions
fun NavController.navigateToLoginRoute(navOptions: NavOptionsBuilder.() -> Unit = {}) =
    navigate(route = LoginRoute, builder = navOptions)

fun NavController.navigateToRegisterScreen(navOptions: NavOptionsBuilder.() -> Unit = {}) =
    navigate(route = RegisterRoute, builder = navOptions)

fun NavController.navigateToCreatePinScreen(
    screenData: CreatePinScreenData,
    navOptions: NavOptionsBuilder.() -> Unit = {}
) = navigate(route = CreatePinRoute(screenData), builder = navOptions)

fun NavController.navigateToConfirmPinScreen(
    screenData: CreatePinScreenData,
    navOptions: NavOptionsBuilder.() -> Unit = {}
) = navigate(route = ConfirmPinRoute(screenData), builder = navOptions)

fun NavController.navigateToPreferencesScreen(
    screenData: PreferencesScreenData,
    navOptions: NavOptionsBuilder.() -> Unit = {}
) = navigate(route = PreferencesRoute(screenData), builder = navOptions)