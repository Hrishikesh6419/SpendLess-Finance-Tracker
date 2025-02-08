package com.hrishi.auth.presentation.create_pin

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hrishi.auth.apresentation.R
import com.hrishi.core.presentation.designsystem.LoginIcon
import com.hrishi.core.presentation.designsystem.SpendLessFinanceTrackerTheme
import com.hrishi.core.presentation.designsystem.components.SpendLessEnterPin
import com.hrishi.core.presentation.designsystem.components.SpendLessPinPad
import com.hrishi.core.presentation.designsystem.components.SpendLessSnackBarHost
import com.hrishi.presentation.ui.ObserveAsEvents
import org.koin.androidx.compose.koinViewModel

@Composable
fun CreatePinScreenRoot(
    modifier: Modifier = Modifier,
    viewModel: CreatePinViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackBarHostState = remember { SnackbarHostState() }

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            CreatePinEvent.NavigateToConfirmPinScreen -> Unit
        }
    }

    CreatePinScreen(
        modifier = modifier,
        snackbarHostState = snackBarHostState,
        uiState = uiState,
        onAction = viewModel::onAction
    )
}

@Composable
fun CreatePinScreen(
    modifier: Modifier = Modifier,
    snackbarHostState: SnackbarHostState,
    uiState: CreatePinState,
    onAction: (CreatePinAction) -> Unit
) {
    Scaffold(containerColor = Color.Transparent, snackbarHost = {
        SpendLessSnackBarHost(snackbarHostState)
    }) { contentPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(contentPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.padding(12.dp))
            Image(
                imageVector = LoginIcon,
                contentDescription = stringResource(R.string.login_button_content_description)
            )
            Text(
                modifier = Modifier.padding(top = 20.dp),
                text = stringResource(R.string.create_pin_headline),
                style = MaterialTheme.typography.headlineMedium,
            )
            Text(
                modifier = Modifier.padding(
                    top = 8.dp,
                ),
                text = stringResource(R.string.create_pin_sub_headline)
            )

            SpendLessEnterPin(
                pin = uiState.pin,
                modifier = Modifier.padding(
                    top = 36.dp,
                    start = 46.dp,
                    end = 45.dp
                )
            )

            SpendLessPinPad(
                modifier = Modifier.padding(top = 32.dp),
                hasBiometricButton = false,
                onNumberPressedClicked = {
                    onAction(CreatePinAction.OnNumberPressed(it))
                },
                onDeletePressedClicked = {
                    onAction(CreatePinAction.OnDeletePressed)
                }
            )
        }
    }
}

@Composable
@Preview
fun PreviewCreatePinScreen() {
    SpendLessFinanceTrackerTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            CreatePinScreen(
                uiState = CreatePinState(),
                snackbarHostState = SnackbarHostState(),
                onAction = {}
            )
        }
    }
}