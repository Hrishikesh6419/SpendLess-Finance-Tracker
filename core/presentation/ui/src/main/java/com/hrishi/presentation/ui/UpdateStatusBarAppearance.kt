package com.hrishi.presentation.ui

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

@Composable
fun UpdateStatusBarAppearance(isDarkStatusBarIcons: Boolean) {
    val context = LocalContext.current
    val view = LocalView.current

    if (context is Activity) {
        val window = context.window
        val insetsController = WindowCompat.getInsetsController(window, view)

        DisposableEffect(isDarkStatusBarIcons) {
            insetsController.isAppearanceLightStatusBars = isDarkStatusBarIcons
            onDispose {
                insetsController.isAppearanceLightStatusBars = true
            }
        }
    }
}