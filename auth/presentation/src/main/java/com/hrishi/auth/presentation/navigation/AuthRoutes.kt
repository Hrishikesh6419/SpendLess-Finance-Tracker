package com.hrishi.auth.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavOptionsBuilder
import com.hrishi.auth.presentation.navigation.model.CreatePinScreenData
import com.hrishi.auth.presentation.navigation.model.PreferencesScreenData
import kotlinx.serialization.Serializable

// Routes
@Serializable
data object AuthBaseRoute

@Serializable
data object LoginRoute

@Serializable
data object RegisterRoute

@Serializable
data class CreatePinRoute(val screenData: CreatePinScreenData)

@Serializable
data class ConfirmPinRoute(val screenData: CreatePinScreenData)

@Serializable
data class PreferencesRoute(val screenData: PreferencesScreenData)

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