package com.hrishi.core.presentation.designsystem.components

import android.app.Activity
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import com.hrishi.core.presentation.designsystem.LocalStatusBarAppearance

@Composable
fun SpendLessScaffold(
    modifier: Modifier = Modifier,
    containerColor: Color = Color.Transparent,
    withGradient: Boolean = false,
    topAppBar: @Composable () -> Unit = {},
    floatingActionButton: @Composable () -> Unit = {},
    snackbarHost: @Composable () -> Unit = {},
    content: @Composable (PaddingValues) -> Unit
) {
    val view = LocalView.current
    val statusBarAppearance = LocalStatusBarAppearance.current

    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars =
                statusBarAppearance.isDarkStatusBarIcons
        }
    }

    Scaffold(
        topBar = topAppBar,
        containerColor = containerColor,
        floatingActionButton = floatingActionButton,
        floatingActionButtonPosition = FabPosition.End,
        snackbarHost = { snackbarHost() },
        modifier = modifier
    ) { padding ->
        if (withGradient) {
            PrimaryGradientBackground {
                content(padding)
            }
        } else {
            content(padding)
        }
    }
}