package com.hrishi.core.presentation.designsystem

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf

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

data class StatusBarAppearance(
    val isDarkStatusBarIcons: Boolean = true
)

val LocalStatusBarAppearance = compositionLocalOf { StatusBarAppearance() }

@Composable
fun SpendLessFinanceTrackerTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = ColorScheme,
        typography = Typography,
        content = content
    )
}