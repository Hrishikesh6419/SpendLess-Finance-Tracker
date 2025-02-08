package com.hrishi.auth.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavOptionsBuilder
import kotlinx.serialization.Serializable

// Routes
@Serializable
data object AuthBaseRoute

@Serializable
data object LoginRoute

@Serializable
data object RegisterRoute

@Serializable
data object CreatePinRoute

// Navigation Extensions
fun NavController.navigateToLoginRoute(navOptions: NavOptionsBuilder.() -> Unit = {}) =
    navigate(route = LoginRoute, navOptions)

fun NavController.navigateToRegisterScreen(navOptions: NavOptionsBuilder.() -> Unit = {}) =
    navigate(route = RegisterRoute, navOptions)

fun NavController.navigateToCreatePinScreen(navOptions: NavOptionsBuilder.() -> Unit = {}) =
    navigate(route = CreatePinRoute, navOptions)