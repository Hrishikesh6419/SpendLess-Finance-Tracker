package com.hrishi.auth.presentation.user_preference

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.hrishi.core.presentation.designsystem.SpendLessFinanceTrackerTheme

@Composable
fun PreferencesScreenRoot(
    modifier: Modifier = Modifier
) {

    PreferencesScreen()
}

@Composable
fun PreferencesScreen(
    modifier: Modifier = Modifier
) {
    Text("Preferences Screen")
}

@Composable
@Preview
fun PreviewPreferencesScreen() {
    SpendLessFinanceTrackerTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            PreferencesScreen()
        }
    }
}

