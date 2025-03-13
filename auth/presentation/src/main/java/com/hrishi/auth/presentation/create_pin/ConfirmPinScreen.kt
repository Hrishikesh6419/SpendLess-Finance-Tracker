package com.hrishi.auth.presentation.create_pin

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hrishi.auth.apresentation.R
import com.hrishi.auth.presentation.create_pin.component.CreatePinScreenComponent
import com.hrishi.core.presentation.designsystem.SpendLessFinanceTrackerTheme
import com.hrishi.presentation.ui.ObserveAsEvents
import com.hrishi.presentation.ui.navigation.PreferencesScreenData
import com.hrishi.presentation.ui.showTimedSnackBar
import org.koin.androidx.compose.koinViewModel

@Composable
fun ConfirmPinScreenRoot(
    modifier: Modifier = Modifier,
    onNavigateToRegisterScreen: () -> Unit,
    onNavigateToPreferencesScreen: (PreferencesScreenData) -> Unit,
    viewModel: CreatePinViewModel = koinViewModel()
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackBarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            CreatePinEvent.NavigateToRegisterScreen -> onNavigateToRegisterScreen()
            CreatePinEvent.PinsDoNotMatch -> {
                scope.showTimedSnackBar(
                    snackBarHostState = snackBarHostState,
                    message = context.getString(R.string.confirm_pin_error_pins_do_not_match)
                )
            }

            is CreatePinEvent.NavigateToPreferencesScreen -> onNavigateToPreferencesScreen(event.screenData)
            is CreatePinEvent.NavigateToConfirmPinScreen -> Unit // Not Applicable here
        }
    }

    CreatePinScreenComponent(
        modifier = modifier,
        headlineResId = R.string.confirm_pin_headline,
        subHeadlineResId = R.string.confirm_pin_sub_headline,
        snackbarHostState = snackBarHostState,
        uiState = uiState,
        onAction = viewModel::onAction
    )
}

@Composable
@Preview
fun PreviewConfirmPinScreenRoot() {
    SpendLessFinanceTrackerTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            CreatePinScreenComponent(
                uiState = CreatePinState(),
                snackbarHostState = SnackbarHostState(),
                onAction = {},
                headlineResId = R.string.confirm_pin_headline,
                subHeadlineResId = R.string.confirm_pin_sub_headline
            )
        }
    }
}