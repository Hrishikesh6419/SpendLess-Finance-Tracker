package com.hrishi.presentation.ui

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.window.DialogWindowProvider
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

@Composable
fun UpdateDialogStatusBarAppearance(isDarkStatusBarIcons: Boolean) {
    val view = LocalView.current

    val dialogWindow = remember {
        (view.parent as? DialogWindowProvider)?.window
    }

    if (dialogWindow != null) {
        DisposableEffect(isDarkStatusBarIcons) {
            val controller = WindowCompat.getInsetsController(dialogWindow, view)
            val originalState = controller.isAppearanceLightStatusBars

            controller.isAppearanceLightStatusBars = isDarkStatusBarIcons

            onDispose {
                controller.isAppearanceLightStatusBars = originalState
            }
        }
    }
}