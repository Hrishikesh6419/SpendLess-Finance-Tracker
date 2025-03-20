package com.hrishi.auth.presentation.login

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hrishi.auth.apresentation.R
import com.hrishi.auth.presentation.login.component.SpendLessClickableText
import com.hrishi.core.presentation.designsystem.LoginIcon
import com.hrishi.core.presentation.designsystem.SpendLessFinanceTrackerTheme
import com.hrishi.core.presentation.designsystem.components.SpendLessScaffold
import com.hrishi.core.presentation.designsystem.components.SpendLessSnackBarHost
import com.hrishi.core.presentation.designsystem.components.buttons.SpendLessButton
import com.hrishi.core.presentation.designsystem.components.text_field.SpendLessTextField
import com.hrishi.presentation.ui.ObserveAsEvents
import com.hrishi.presentation.ui.showTimedSnackBar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import org.koin.androidx.compose.koinViewModel

@Composable
fun LoginScreenRoot(
    modifier: Modifier = Modifier,
    onRegisterClick: () -> Unit,
    onNavigateToDashboard: () -> Unit,
    viewModel: LoginViewModel = koinViewModel()
) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackBarHostState = remember { SnackbarHostState() }
    val keyboardController = LocalSoftwareKeyboardController.current

    EventHandler(
        events = viewModel.events,
        scope = scope,
        snackBarHostState = snackBarHostState,
        context = context,
        keyboardController = keyboardController,
        onRegisterClick = onRegisterClick,
        onNavigateToDashboard = onNavigateToDashboard
    )

    LoginScreen(
        modifier = modifier,
        uiState = uiState,
        snackbarHostState = snackBarHostState,
        onAction = viewModel::onAction
    )
}

@Composable
private fun EventHandler(
    events: Flow<LoginEvent>,
    scope: CoroutineScope,
    snackBarHostState: SnackbarHostState,
    context: Context,
    keyboardController: SoftwareKeyboardController?,
    onRegisterClick: () -> Unit,
    onNavigateToDashboard: () -> Unit
) {
    ObserveAsEvents(events) { event ->
        when (event) {
            LoginEvent.IncorrectCredentials -> {
                scope.showTimedSnackBar(
                    snackBarHostState = snackBarHostState,
                    message = context.getString(R.string.login_error_username_or_pin_is_incorrect)
                )
            }

            LoginEvent.NavigateToRegisterScreen -> {
                keyboardController?.hide()
                onRegisterClick()
            }

            LoginEvent.NavigateToDashboardScreen -> {
                keyboardController?.hide()
                onNavigateToDashboard()
            }
        }
    }
}

@Composable
private fun LoginScreen(
    modifier: Modifier = Modifier,
    uiState: LoginViewState,
    snackbarHostState: SnackbarHostState,
    onAction: (LoginAction) -> Unit
) {
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    SpendLessScaffold(
        snackbarHost = { SpendLessSnackBarHost(snackbarHostState) }
    ) { contentPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(contentPadding)
                .windowInsetsPadding(WindowInsets.systemBars),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.padding(top = 24.dp))
            LoginWelcomeSection()
            Spacer(modifier = Modifier.padding(top = 36.dp))
            LoginFields(
                uiState = uiState,
                focusRequester = focusRequester,
                onAction = onAction
            )
            Spacer(modifier = Modifier.padding(top = 24.dp))
            LoginFooterSection(onAction = onAction)
        }
    }
}

@Composable
private fun LoginWelcomeSection() {
    Image(
        imageVector = LoginIcon,
        contentDescription = stringResource(R.string.login_button_content_description)
    )
    Text(
        modifier = Modifier.padding(top = 20.dp),
        text = stringResource(R.string.login_welcome_back),
        style = MaterialTheme.typography.headlineMedium,
    )
    Text(
        modifier = Modifier.padding(top = 8.dp),
        text = stringResource(R.string.login_enter_your_details),
        style = MaterialTheme.typography.bodyMedium
    )
}

@Composable
private fun LoginFields(
    uiState: LoginViewState,
    focusRequester: FocusRequester,
    onAction: (LoginAction) -> Unit
) {
    SpendLessTextField(
        value = uiState.username,
        onValueChange = { onAction(LoginAction.OnUsernameUpdate(it)) },
        hint = stringResource(R.string.login_username),
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .focusRequester(focusRequester),
    )

    Spacer(modifier = Modifier.padding(top = 16.dp))

    SpendLessTextField(
        value = uiState.pin,
        onValueChange = { onAction(LoginAction.OnPinChange(it)) },
        hint = stringResource(R.string.login_pin),
        keyboardType = KeyboardType.Number,
        imeAction = ImeAction.Done,
        onDone = { onAction(LoginAction.OnLoginClick) },
        modifier = Modifier.padding(horizontal = 16.dp)
    )
}

@Composable
private fun LoginFooterSection(
    onAction: (LoginAction) -> Unit
) {
    SpendLessButton(
        modifier = Modifier.padding(horizontal = 16.dp),
        buttonText = stringResource(R.string.login_log_in),
    ) {
        onAction(LoginAction.OnLoginClick)
    }

    Spacer(modifier = Modifier.padding(top = 28.dp))

    SpendLessClickableText(
        text = stringResource(R.string.login_new_to_spend_less)
    ) {
        onAction(LoginAction.OnRegisterClick)
    }
}

@Preview
@Composable
private fun PreviewLoginScreen() {
    SpendLessFinanceTrackerTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            LoginScreen(
                modifier = Modifier,
                uiState = LoginViewState(),
                snackbarHostState = SnackbarHostState(),
                onAction = {}
            )
        }
    }
}