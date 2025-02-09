package com.hrishi.auth.presentation.create_pin.component

import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
    Scaffold(containerColor = Color.Transparent,
        snackbarHost = {
            SpendLessSnackBarHost(snackbarHostState)
        },
        topBar = {
            SpendLessTopBar(
                onStartIconClick = {
                    onAction(CreatePinAction.OnBackPressed)
                }
            )
        }
    ) { contentPadding ->
        Box( // Wrap everything inside a Box to avoid content overlapping with TopBar when using vertical scroll
            modifier = modifier
                .fillMaxSize()
                .padding(contentPadding)
        ) {
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Image(
                    imageVector = LoginIcon,
                    contentDescription = stringResource(R.string.login_button_content_description)
                )
                Text(
                    modifier = Modifier.padding(top = 20.dp),
                    text = stringResource(headlineResId),
                    style = MaterialTheme.typography.headlineMedium,
                )
                Text(
                    modifier = Modifier.padding(
                        top = 8.dp,
                    ),
                    text = stringResource(subHeadlineResId)
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
                    hasBiometricButton = true,
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