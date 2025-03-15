package com.hrishi.auth.presentation.create_pin

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hrishi.auth.apresentation.R
import com.hrishi.auth.presentation.create_pin.component.CreatePinScreenComponent
import com.hrishi.core.presentation.designsystem.SpendLessFinanceTrackerTheme
import com.hrishi.presentation.ui.ObserveAsEvents
import com.hrishi.presentation.ui.navigation.CreatePinScreenData
import org.koin.androidx.compose.koinViewModel

@Composable
fun CreatePinScreenRoot(
    modifier: Modifier = Modifier,
    onNavigateToConfirmScreen: (CreatePinScreenData) -> Unit,
    onBackClick: () -> Unit,
    viewModel: CreatePinViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackBarHostState = remember { SnackbarHostState() }

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            is CreatePinEvent.NavigateToConfirmPinScreen -> onNavigateToConfirmScreen(event.screenData)
            CreatePinEvent.OnBackClick -> onBackClick()
            else -> Unit
        }
    }

    CreatePinScreenComponent(
        modifier = modifier,
        headlineResId = R.string.create_pin_headline,
        subHeadlineResId = R.string.create_pin_sub_headline,
        snackbarHostState = snackBarHostState,
        uiState = uiState,
        onAction = viewModel::onAction
    )
}

@Composable
@Preview
fun PreviewCreatePinScreen() {
    SpendLessFinanceTrackerTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            CreatePinScreenComponent(
                uiState = CreatePinState(),
                snackbarHostState = SnackbarHostState(),
                onAction = {},
                headlineResId = R.string.create_pin_headline,
                subHeadlineResId = R.string.create_pin_sub_headline
            )
        }
    }
}