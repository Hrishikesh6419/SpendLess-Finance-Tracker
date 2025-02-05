package com.hrishi.core.presentation.designsystem.components

import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable

@Composable
fun SpendLessSnackBarHost(snackBarHostState: SnackbarHostState) {
    SnackbarHost(hostState = snackBarHostState) { data ->
        SpendLessErrorBanner(
            text = data.visuals.message
        )
    }
}