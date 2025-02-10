package com.hrishi.auth.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavOptionsBuilder
import com.hrishi.auth.presentation.navigation.model.CreatePinData
import kotlinx.serialization.Serializable

// Routes
@Serializable
data object AuthBaseRoute

@Serializable
data object LoginRoute

@Serializable
data object RegisterRoute

@Serializable
data class CreatePinRoute(val screenData: CreatePinData)

@Serializable
data class ConfirmPinRoute(val screenData: CreatePinData)

// Navigation Extensions
fun NavController.navigateToLoginRoute(navOptions: NavOptionsBuilder.() -> Unit = {}) =
    navigate(route = LoginRoute, navOptions)

fun NavController.navigateToRegisterScreen(navOptions: NavOptionsBuilder.() -> Unit = {}) =
    navigate(route = RegisterRoute, navOptions)

fun NavController.navigateToCreatePinScreen(
    screenData: CreatePinData,
    navOptions: NavOptionsBuilder.() -> Unit = {}
) =
    navigate(route = CreatePinRoute(screenData), navOptions)

fun NavController.navigateToConfirmPinScreen(
    screenData: CreatePinData,
    navOptions: NavOptionsBuilder.() -> Unit = {}
) = navigate(route = ConfirmPinRoute(screenData), navOptions)