package com.spendless.session_management.presentation.pin_prompt

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun PinPromptScreenRoot(
    onSuccessClick: () -> Unit
) {
    PinPromptScreen(onSuccessClick = onSuccessClick)
}

@Composable
fun PinPromptScreen(
    onSuccessClick: () -> Unit
) {
    Column {
        Text("Pin Prompt Screen")
        Button(onClick = onSuccessClick) {
            Text("Success Click")
        }
    }
}

@Composable
@Preview
fun PreviewPinPromptScreen() {
    PinPromptScreen() {

    }
}