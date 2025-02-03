package com.hrishi.auth.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavOptions
import kotlinx.serialization.Serializable

// Routes
@Serializable
data object AuthBaseRoute

@Serializable
data object LoginRoute

@Serializable
data object RegisterRoute

// Navigation Extensions
fun NavController.navigateToLoginRoute(navOptions: NavOptions? = null) =
    navigate(route = LoginRoute, navOptions)

fun NavController.navigateToRegisterScreen(navOptions: NavOptions? = null) =
    navigate(route = RegisterRoute, navOptions)