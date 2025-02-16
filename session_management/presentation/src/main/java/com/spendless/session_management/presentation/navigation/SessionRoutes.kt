package com.spendless.session_management.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavOptionsBuilder
import kotlinx.serialization.Serializable

@Serializable
data object SessionBaseRoute

@Serializable
data object PinPromptScreenRoute

fun NavController.navigateToPinPromptScreen(
    navOptions: NavOptionsBuilder.() -> Unit = {}
) = navigate(PinPromptScreenRoute, navOptions)