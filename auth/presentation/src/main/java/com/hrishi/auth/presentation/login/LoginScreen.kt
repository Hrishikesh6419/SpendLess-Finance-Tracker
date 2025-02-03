package com.hrishi.auth.presentation.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun LoginScreen(
    onRegisterClick: () -> Unit
) {
    Column(verticalArrangement = Arrangement.Center) {
        Text("Login Screen")
        Button(onClick = {
            onRegisterClick()
        }) {
            Text("Navigate")
        }
    }
}

@Preview
@Composable
fun PreviewLoginScreen() {
    LoginScreen { }
}