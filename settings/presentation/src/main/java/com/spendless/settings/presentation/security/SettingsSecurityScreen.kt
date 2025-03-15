package com.spendless.settings.presentation.security

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hrishi.core.domain.model.BiometricPromptStatus
import com.hrishi.core.domain.model.LockoutDuration
import com.hrishi.core.domain.model.SessionDuration
import com.hrishi.core.presentation.designsystem.SpendLessFinanceTrackerTheme
import com.hrishi.core.presentation.designsystem.components.SegmentedSelector
import com.hrishi.core.presentation.designsystem.components.SpendLessScaffold
import com.hrishi.core.presentation.designsystem.components.SpendLessTopBar
import com.hrishi.core.presentation.designsystem.components.buttons.SpendLessButton
import com.hrishi.presentation.ui.LocalAuthActionHandler
import com.hrishi.presentation.ui.ObserveAsEvents
import org.koin.androidx.compose.koinViewModel

@Composable
fun SettingsSecurityScreenRoot(
    modifier: Modifier = Modifier,
    onNavigateBack: () -> Unit,
    viewModel: SettingsSecurityViewModel = koinViewModel(),
) {
    val authActionHandler = LocalAuthActionHandler.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            SettingsSecurityEvent.NavigateBack -> onNavigateBack()
            SettingsSecurityEvent.SecuritySettingsSaved -> {
                Toast.makeText(
                    context,
                    "Security Preferences saved successfully!",
                    Toast.LENGTH_SHORT
                ).show()
                onNavigateBack()
            }
        }
    }

    SettingsSecurityScreen(
        modifier = modifier,
        uiState = uiState,
        onAction = { action ->
            when (action) {
                SettingsSecurityAction.OnSaveClicked -> {
                    authActionHandler?.invoke {
                        viewModel.onAction(action)
                    }
                }

                else -> viewModel.onAction(action)
            }
        }
    )
}

@Composable
fun SettingsSecurityScreen(
    modifier: Modifier = Modifier,
    uiState: SettingsSecurityViewState,
    onAction: (SettingsSecurityAction) -> Unit
) {
    SpendLessScaffold(
        containerColor = Color.Transparent,
        topBar = {
            SpendLessTopBar(
                modifier = Modifier
                    .padding(horizontal = 8.dp),
                title = "Security",
                titleColor = MaterialTheme.colorScheme.onSurface,
                onStartIconClick = {
                    onAction(SettingsSecurityAction.OnBackClicked)
                }
            )
        }) { contentPadding ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(contentPadding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {

                SegmentedSelector(
                    title = "Biometrics for PIN prompt",
                    options = BiometricPromptStatus.entries.toTypedArray(),
                    selectedOption = uiState.biometricPromptStatus,
                    onOptionSelected = {
                        onAction(SettingsSecurityAction.OnBiometricSettingUpdated(it))
                    },
                    displayText = {
                        it.getDisplayValue()
                    }
                )

                SegmentedSelector(
                    modifier = Modifier.padding(top = 16.dp),
                    title = "Session expiry duration",
                    options = SessionDuration.entries.toTypedArray(),
                    selectedOption = uiState.sessionExpiryDuration,
                    onOptionSelected = {
                        onAction(SettingsSecurityAction.OnSessionExpiryUpdated(it))
                    },
                    displayText = {
                        it.displayText()
                    }
                )

                SegmentedSelector(
                    modifier = Modifier.padding(top = 16.dp),
                    title = "Locked out duration",
                    options = LockoutDuration.entries.toTypedArray(),
                    selectedOption = uiState.lockedOutDuration,
                    onOptionSelected = {
                        onAction(SettingsSecurityAction.OnLockOutDurationUpdated(it))
                    },
                    displayText = {
                        it.displayText()
                    }
                )

                SpendLessButton(
                    isEnabled = uiState.enableSaveButton,
                    modifier = Modifier.padding(vertical = 34.dp),
                    buttonText = "Save"
                ) {
                    onAction(SettingsSecurityAction.OnSaveClicked)
                }
            }
        }
    }

}

@Preview
@Composable
fun PreviewSettingsSecurityScreen() {
    SpendLessFinanceTrackerTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            SettingsSecurityScreen(
                modifier = Modifier,
                uiState = SettingsSecurityViewState(
                    biometricPromptStatus = BiometricPromptStatus.ENABLE,
                    sessionExpiryDuration = SessionDuration.ONE_MIN,
                    lockedOutDuration = LockoutDuration.FIFTEEN_SECONDS
                ),
                onAction = {

                }
            )
        }
    }
}