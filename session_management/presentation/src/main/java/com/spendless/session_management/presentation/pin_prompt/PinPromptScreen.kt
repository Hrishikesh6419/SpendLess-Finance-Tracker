package com.spendless.session_management.presentation.pin_prompt

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hrishi.core.presentation.designsystem.ExitIcon
import com.hrishi.core.presentation.designsystem.LoginIcon
import com.hrishi.core.presentation.designsystem.SpendLessFinanceTrackerTheme
import com.hrishi.core.presentation.designsystem.components.SpendLessEnterPin
import com.hrishi.core.presentation.designsystem.components.SpendLessPinPad
import com.hrishi.core.presentation.designsystem.components.SpendLessScaffold
import com.hrishi.core.presentation.designsystem.components.SpendLessSnackBarHost
import com.hrishi.core.presentation.designsystem.components.SpendLessTopBar
import com.hrishi.presentation.ui.ObserveAsEvents
import com.hrishi.presentation.ui.formatToTimeString
import com.hrishi.presentation.ui.showTimedSnackBar
import com.spendless.session_management.presentation.R
import org.koin.androidx.compose.koinViewModel

@Composable
fun PinPromptScreenRoot(
    modifier: Modifier = Modifier,
    onSuccessClick: () -> Unit,
    onLogout: () -> Unit,
    viewModel: PinPromptViewModel = koinViewModel()
) {

    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackBarHostState = remember { SnackbarHostState() }

    BackHandler(enabled = true) {

    }

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            PinPromptEvent.OnLogout -> onLogout()
            PinPromptEvent.OnSuccessPopBack -> onSuccessClick()
            PinPromptEvent.IncorrectPin -> {
                scope.showTimedSnackBar(
                    snackBarHostState = snackBarHostState,
                    message = context.getString(R.string.error_incorrect_pin)
                )
            }
        }
    }

    PinPromptScreen(
        modifier = modifier,
        snackbarHostState = snackBarHostState,
        uiState = uiState,
        onAction = viewModel::onAction
    )
}

@Composable
private fun PinPromptScreen(
    modifier: Modifier = Modifier,
    snackbarHostState: SnackbarHostState,
    uiState: PinPromptState,
    onAction: (PinPromptAction) -> Unit
) {
    SpendLessScaffold(
        containerColor = Color.Transparent,
        snackbarHost = {
            SpendLessSnackBarHost(snackbarHostState)
        },
        topBar = {
            SpendLessTopBar(
                startIcon = null,
                endIcon2 = ExitIcon,
                endIcon2Color = MaterialTheme.colorScheme.error,
                endIcon2BackgroundColor = MaterialTheme.colorScheme.error.copy(alpha = 0.08f),
                onEndIcon2Click = {
                    onAction(PinPromptAction.OnLogoutClicked)
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
                    text = if (uiState.isExceededFailedAttempts) {
                        stringResource(R.string.error_too_many_failed_attempts)
                    } else {
                        stringResource(R.string.pin_prompt_headline, uiState.username)
                    },
                    style = MaterialTheme.typography.headlineMedium,
                )

                if (uiState.isExceededFailedAttempts) {
                    LockoutCountdownText(lockoutTimeRemaining = uiState.lockoutTimeRemaining)
                } else {
                    Text(
                        modifier = Modifier.padding(top = 8.dp),
                        text = stringResource(R.string.pin_prompt_sub_headline)
                    )
                }

                SpendLessEnterPin(
                    isLocked = uiState.isExceededFailedAttempts,
                    pin = uiState.pin,
                    modifier = Modifier.padding(
                        top = 36.dp,
                        start = 46.dp,
                        end = 45.dp
                    )
                )

                SpendLessPinPad(
                    isLocked = uiState.isExceededFailedAttempts,
                    modifier = Modifier.padding(top = 32.dp),
                    hasBiometricButton = uiState.isBiometricsEnabled,
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
fun LockoutCountdownText(lockoutTimeRemaining: Long) {
    val textStyle = MaterialTheme.typography.bodyMedium

    Text(
        modifier = Modifier.padding(top = 8.dp),
        text = buildAnnotatedString {
            withStyle(
                style = SpanStyle(
                    fontFamily = textStyle.fontFamily,
                    color = textStyle.color,
                    fontSize = textStyle.fontSize,
                    fontWeight = FontWeight.Normal
                )
            ) {
                append(stringResource(R.string.error_try_pin_again))
            }
            withStyle(
                style = SpanStyle(
                    fontFamily = textStyle.fontFamily,
                    color = textStyle.color,
                    fontSize = textStyle.fontSize,
                    fontWeight = FontWeight.Bold
                )
            ) {
                append(" ")
                append(lockoutTimeRemaining.formatToTimeString())
            }
        }
    )
}


@Composable
@Preview
fun PreviewPinPromptScreen() {
    SpendLessFinanceTrackerTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            PinPromptScreen(
                uiState = PinPromptState(
                    username = "Hrishi"
                ),
                snackbarHostState = SnackbarHostState(),
                onAction = {},
            )
        }
    }
}