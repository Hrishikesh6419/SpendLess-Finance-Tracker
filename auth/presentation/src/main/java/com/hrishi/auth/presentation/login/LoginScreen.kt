package com.hrishi.auth.presentation.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hrishi.auth.apresentation.R
import com.hrishi.auth.presentation.login.component.SpendLessClickableText
import com.hrishi.core.presentation.designsystem.LoginIcon
import com.hrishi.core.presentation.designsystem.SpendLessFinanceTrackerTheme
import com.hrishi.core.presentation.designsystem.components.SpendLessButton
import com.hrishi.core.presentation.designsystem.components.SpendLessTextField
import org.koin.androidx.compose.koinViewModel

@Composable
fun LoginScreenRoot(
    modifier: Modifier = Modifier,
    onRegisterClick: () -> Unit,
    viewModel: LoginViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LoginScreen(
        modifier = modifier,
        uiState = uiState,
        onRegisterClick = onRegisterClick,
        onAction = viewModel::onAction
    )
}

@Composable
fun LoginScreen(
    modifier: Modifier,
    uiState: LoginViewState,
    onRegisterClick: () -> Unit,
    onAction: (LoginAction) -> Unit
) {
    Scaffold(
        containerColor = Color.Transparent
    ) { contentPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(contentPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.padding(24.dp))
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

            SpendLessTextField(
                state = uiState.username,
                hint = stringResource(R.string.login_username),
                modifier = Modifier.padding(
                    start = 16.dp,
                    end = 16.dp,
                    top = 36.dp
                )
            )

            SpendLessTextField(
                state = uiState.pin,
                hint = stringResource(R.string.login_pin),
                modifier = Modifier.padding(
                    start = 16.dp,
                    end = 16.dp,
                    top = 16.dp
                )
            )

            SpendLessButton(
                modifier = Modifier.padding(
                    start = 16.dp,
                    end = 16.dp,
                    top = 24.dp
                ),
                buttonText = stringResource(R.string.login_log_in)
            ) {

            }

            SpendLessClickableText(
                modifier = Modifier.padding(top = 28.dp),
                text = stringResource(R.string.login_new_to_spend_less)
            ) {

            }
        }
    }
}

@Preview
@Composable
fun PreviewLoginScreen() {
    SpendLessFinanceTrackerTheme {
        Surface(color = MaterialTheme.colorScheme.onBackground) {
            LoginScreen(
                modifier = Modifier,
                uiState = LoginViewState(),
                onRegisterClick = {}
            ) {

            }
        }
    }
}