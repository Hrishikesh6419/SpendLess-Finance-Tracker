package com.spendless.dashboard.presentation.dashboard

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.hrishi.presentation.ui.ObserveAsEvents
import org.koin.androidx.compose.koinViewModel

@Composable
fun DashboardScreenRoot(
    viewModel: DashboardViewModel = koinViewModel()
) {

    ObserveAsEvents(viewModel.events) {
        when (it) {

            DashboardEvent.NavigateTest -> {

            }
        }
    }

    DashboardScreen {
        viewModel.triggerPin()
    }
}

@Composable
fun DashboardScreen(
    onClick: () -> Unit
) {
    Column {
        Text("Da")
        Button(onClick = onClick) {
            Text("Navigate")
        }
    }
}

@Composable
@Preview
fun PreviewDashboardScreen() {
    DashboardScreen {
    }
}

