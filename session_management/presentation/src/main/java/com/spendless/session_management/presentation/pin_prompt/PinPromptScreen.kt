package com.spendless.session_management.presentation.pin_prompt

import androidx.activity.compose.BackHandler
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
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
import com.hrishi.core.presentation.designsystem.LoginIcon
import com.hrishi.core.presentation.designsystem.SpendLessFinanceTrackerTheme
import com.hrishi.core.presentation.designsystem.components.SpendLessEnterPin
import com.hrishi.core.presentation.designsystem.components.SpendLessPinPad
import com.hrishi.core.presentation.designsystem.components.SpendLessSnackBarHost
import com.hrishi.core.presentation.designsystem.components.SpendLessTopBar
import com.hrishi.presentation.ui.ObserveAsEvents
import com.spendless.session_management.presentation.R
import org.koin.androidx.compose.koinViewModel

@Composable
fun PinPromptScreenRoot(
    modifier: Modifier = Modifier,
    onSuccessClick: () -> Unit,
    viewModel: PinPromptViewModel = koinViewModel()
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackBarHostState = remember { SnackbarHostState() }

    BackHandler(enabled = true) {

    }

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            PinPromptEvent.OnLogout -> Unit
            PinPromptEvent.OnSuccessPopBack -> onSuccessClick()
            PinPromptEvent.WrongPin -> Unit
        }
    }

    PinPromptScreen(
        modifier = modifier,
        headlineResId = R.string.pin_prompt_headline,
        subHeadlineResId = R.string.pin_prompt_sub_headline,
        snackbarHostState = snackBarHostState,
        uiState = uiState,
        onAction = viewModel::onAction
    )
}

@Composable
fun PinPromptScreen(
    modifier: Modifier = Modifier,
    @StringRes headlineResId: Int,
    @StringRes subHeadlineResId: Int,
    snackbarHostState: SnackbarHostState,
    uiState: PinPromptState,
    onAction: (PinPromptAction) -> Unit
) {
    Scaffold(containerColor = Color.Transparent,
        snackbarHost = {
            SpendLessSnackBarHost(snackbarHostState)
        },
    ) { contentPadding ->
        Box( // Wrap everything inside a Box to avoid content overlapping with TopBar when using vertical scroll
            modifier = modifier
                .fillMaxSize()
                .padding(contentPadding)
        ) {
            Column(
                modifier = Modifier
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
                        onAction(PinPromptAction.OnNumberPressed(it))
                    },
                    onDeletePressedClicked = {
                        onAction(PinPromptAction.OnDeletePressed)
                    }
                )
            }
        }
    }
}

@Composable
@Preview
fun PreviewPinPromptScreen() {
    SpendLessFinanceTrackerTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            PinPromptScreen(
                uiState = PinPromptState(),
                snackbarHostState = SnackbarHostState(),
                onAction = {},
                headlineResId = R.string.pin_prompt_headline,
                subHeadlineResId = R.string.pin_prompt_sub_headline
            )
        }
    }
}