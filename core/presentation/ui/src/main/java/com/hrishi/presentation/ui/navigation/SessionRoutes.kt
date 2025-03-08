package com.hrishi.presentation.ui.navigation

import androidx.navigation.NavController
import androidx.navigation.NavOptionsBuilder
import kotlinx.serialization.Serializable

@Serializable
data object SessionBaseRoute : AppRoute

@Serializable
data object PinPromptScreenRoute : AppRoute

fun NavController.navigateToPinPromptScreen(
    navOptions: NavOptionsBuilder.() -> Unit = {}
) = navigate(PinPromptScreenRoute, navOptions)