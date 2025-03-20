package com.hrishi.auth.presentation.create_pin.component

import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hrishi.auth.apresentation.R
import com.hrishi.auth.presentation.create_pin.CreatePinAction
import com.hrishi.auth.presentation.create_pin.CreatePinState
import com.hrishi.core.presentation.designsystem.LoginIcon
import com.hrishi.core.presentation.designsystem.SpendLessFinanceTrackerTheme
import com.hrishi.core.presentation.designsystem.components.SpendLessEnterPin
import com.hrishi.core.presentation.designsystem.components.SpendLessPinPad
import com.hrishi.core.presentation.designsystem.components.SpendLessScaffold
import com.hrishi.core.presentation.designsystem.components.SpendLessSnackBarHost
import com.hrishi.core.presentation.designsystem.components.SpendLessTopBar

@Composable
fun CreatePinScreenComponent(
    modifier: Modifier = Modifier,
    @StringRes headlineResId: Int,
    @StringRes subHeadlineResId: Int,
    snackbarHostState: SnackbarHostState,
    uiState: CreatePinState,
    onAction: (CreatePinAction) -> Unit
) {
    SpendLessScaffold(
        containerColor = Color.Transparent,
        snackbarHost = { SpendLessSnackBarHost(snackbarHostState) },
        topBar = {
            SpendLessTopBar(
                onStartIconClick = {
                    onAction(CreatePinAction.OnBackPressed)
                }
            )
        }
    ) { contentPadding ->
        CreatePinContent(
            modifier = modifier.padding(contentPadding),
            headlineResId = headlineResId,
            subHeadlineResId = subHeadlineResId,
            uiState = uiState,
            onAction = onAction
        )
    }
}

@Composable
private fun CreatePinContent(
    modifier: Modifier = Modifier,
    @StringRes headlineResId: Int,
    @StringRes subHeadlineResId: Int,
    uiState: CreatePinState,
    onAction: (CreatePinAction) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 26.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            imageVector = LoginIcon,
            contentDescription = stringResource(R.string.login_button_content_description)
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = stringResource(headlineResId),
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = stringResource(subHeadlineResId),
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(modifier = Modifier.height(36.dp))

        SpendLessEnterPin(
            pin = uiState.pin,
            modifier = Modifier.padding(horizontal = 46.dp)
        )

        Spacer(modifier = Modifier.height(32.dp))

        SpendLessPinPad(
            hasBiometricButton = false, // TODO: Remove this
            onNumberPressedClicked = { onAction(CreatePinAction.OnNumberPressed(it)) },
            onDeletePressedClicked = { onAction(CreatePinAction.OnDeletePressed) }
        )
    }
}

@Preview
@Composable
fun PreviewCreatePinScreenComponent() {
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