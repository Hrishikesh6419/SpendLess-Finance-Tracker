package com.hrishi.core.presentation.designsystem

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

val primaryFixed = SpendLessPrimaryFixed
val onPrimaryFixed = SpendLessOnPrimaryFixed
val secondaryFixed = SpendLessSecondaryFixed
val secondaryFixedDim = SpendLessSecondaryFixedDim
val success = SpendLessSuccess

private val ColorScheme = darkColorScheme(
    primary = SpendLessPurple,
    onPrimary = SpendLessWhite,
    secondary = SpendLessSecondary,
    onSurface = SpendLessBlack,
    onSurfaceVariant = SpendLessDarkGrey,
    error = SpendLessRed,
    background = SpendLessPaleLavender,
    onBackground = SpendLessLightGrey,
    primaryContainer = SpendLessPrimaryContainer,
    onPrimaryContainer = SpendLessOnPrimaryContainer,
    surfaceContainerLowest = SpendLessSurfaceContainerLowest,
    surfaceContainerLow = SpendLessSurfaceContainerLow,
    inversePrimary = SpendLessInversePrimary,
    secondaryContainer = SpendLessSecondaryContainer,
    onSecondaryContainer = SpendLessOnSecondaryContainer,
    tertiaryContainer = SpendLessTertiaryContainer
    )

@Composable
fun SpendLessFinanceTrackerTheme(
    content: @Composable () -> Unit
) {
    val colorScheme = ColorScheme
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}