package com.hrishi.presentation.ui

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

const val MAX_PIN_LENGTH = 5

fun Long.formatToTimeString(): String {
    val minutes = this / 60
    val seconds = this % 60
    return "%02d:%02d".format(minutes, seconds)
}

fun CoroutineScope.showTimedSnackBar(
    snackBarHostState: SnackbarHostState,
    message: String,
    durationMillis: Long = 2000
) {
    launch {
        snackBarHostState.currentSnackbarData?.dismiss()
        val job = launch {
            snackBarHostState.showSnackbar(
                message = message,
                duration = SnackbarDuration.Indefinite
            )
        }
        delay(durationMillis)
        job.cancel()
        snackBarHostState.currentSnackbarData?.dismiss()
    }
}