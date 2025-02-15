package com.hrishi.auth.presentation.user_preference

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hrishi.auth.apresentation.R
import com.hrishi.core.domain.model.Currency
import com.hrishi.core.domain.model.DecimalSeparator
import com.hrishi.core.domain.model.ExpenseFormat
import com.hrishi.core.domain.model.ThousandsSeparator
import com.hrishi.core.presentation.designsystem.SpendLessFinanceTrackerTheme
import com.hrishi.core.presentation.designsystem.components.CurrencySelector
import com.hrishi.core.presentation.designsystem.components.SegmentedSelector
import com.hrishi.core.presentation.designsystem.components.SpendLessButton
import com.hrishi.core.presentation.designsystem.components.SpendLessSnackBarHost
import com.hrishi.core.presentation.designsystem.components.SpendLessTopBar
import com.hrishi.presentation.ui.ObserveAsEvents
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun OnboardingPreferencesScreenRoot(
    modifier: Modifier = Modifier,
    viewModel: OnboardingPreferencesViewModel = koinViewModel(),
    onNavigateToDashboardScreen: () -> Unit,
    onNavigateToRegisterScreen: () -> Unit,
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackBarHostState = remember { SnackbarHostState() }

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            OnboardingPreferencesEvent.NavigateToDashboardScreen -> {
                onNavigateToDashboardScreen()
            }

            OnboardingPreferencesEvent.NavigateToRegisterScreen -> {
                onNavigateToRegisterScreen()
            }
            is OnboardingPreferencesEvent.Error -> {
                snackBarHostState.currentSnackbarData?.dismiss()
                when (event) {
                    OnboardingPreferencesEvent.Error.DuplicateEntry -> {
                        scope.launch {
                            snackBarHostState.showSnackbar(
                                message = context.getString(R.string.common_error_username_taken),
                                duration = SnackbarDuration.Short
                            )
                        }
                    }

                    OnboardingPreferencesEvent.Error.Generic -> {
                        scope.launch {
                            snackBarHostState.showSnackbar(
                                message = context.getString(R.string.common_error_something_went_wrong),
                                duration = SnackbarDuration.Short
                            )
                        }
                    }
                }
            }
        }
    }

    OnboardingPreferencesScreen(
        modifier = modifier,
        uiState = uiState,
        snackBarHostState = snackBarHostState,
        onAction = viewModel::onAction
    )
}

@Composable
fun OnboardingPreferencesScreen(
    modifier: Modifier = Modifier,
    uiState: OnboardingPreferencesViewState,
    snackBarHostState: SnackbarHostState,
    onAction: (OnboardingPreferencesAction) -> Unit
) {

    Scaffold(containerColor = Color.Transparent,
        topBar = {
            SpendLessTopBar(
                onStartIconClick = {
                    onAction(OnboardingPreferencesAction.OnBackClicked)
                }
            )
        },
        snackbarHost = {
            SpendLessSnackBarHost(snackBarHostState)
        }) { contentPadding ->
        Box( // Wrap everything inside a Box to avoid content overlapping with TopBar when using vertical scroll
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
                Text(
                    modifier = modifier
                        .padding(top = 42.dp)
                        .fillMaxWidth(),
                    text = stringResource(R.string.onboarding_preferences_headline),
                    style = MaterialTheme.typography.headlineMedium
                )

                Text(
                    modifier = modifier
                        .padding(top = 8.dp)
                        .fillMaxWidth(),
                    text = stringResource(R.string.onboarding_preferences_sub_headline),
                    style = MaterialTheme.typography.bodyMedium
                )

                ExampleFormat(modifier, uiState)

                SegmentedSelector(
                    modifier = Modifier.padding(top = 20.dp),
                    title = stringResource(R.string.onboarding_preferences_expenses_format_title),
                    options = ExpenseFormat.entries.toTypedArray(),
                    selectedOption = uiState.expenseFormat,
                    onOptionSelected = {
                        onAction(OnboardingPreferencesAction.OnExpenseFormatUpdate(it))
                    },
                    displayText = {
                        it.displayText(
                            number = -10.00,
                            currency = uiState.currency,
                            keepDecimal = false
                        )
                    }
                )

                CurrencySelector(
                    modifier = Modifier.padding(top = 16.dp),
                    title = stringResource(R.string.onboarding_preferences_currency_title),
                    selectedOption = uiState.currency,
                    options = Currency.entries.toTypedArray(),
                    currencyDisplay = { it.symbol },
                    currencyTitleDisplay = { it.title },
                    currencyMenuDisplay = { it.symbol },
                    currencyTitleMenuDisplay = { it.title },
                    onItemSelected = {
                        onAction(OnboardingPreferencesAction.OnCurrencyUpdate(it))
                    }
                )

                SegmentedSelector(
                    modifier = Modifier.padding(top = 16.dp),
                    title = stringResource(R.string.onboarding_preferences_decimal_separator_title),
                    options = DecimalSeparator.entries.toTypedArray(),
                    selectedOption = uiState.decimalSeparator,
                    onOptionSelected = {
                        onAction(OnboardingPreferencesAction.OnDecimalSeparatorUpdate(it))
                    },
                    displayText = {
                        it.displayText(
                            number = 1.00,
                            keepDecimal = true
                        )
                    }
                )

                SegmentedSelector(
                    modifier = Modifier.padding(top = 16.dp),
                    title = stringResource(R.string.onboarding_preferences_thousands_separator_title),
                    options = ThousandsSeparator.entries.toTypedArray(),
                    selectedOption = uiState.thousandsSeparator,
                    onOptionSelected = {
                        onAction(OnboardingPreferencesAction.OnThousandsSeparatorUpdate(it))
                    },
                    displayText = {
                        it.displayText(
                            number = 1000.00,
                            keepDecimal = false
                        )
                    }
                )

                SpendLessButton(
                    isEnabled = uiState.enableStartTracking,
                    modifier = Modifier.padding(vertical = 34.dp),
                    buttonText = stringResource(R.string.onboarding_preferences_start_button_title)
                ) {
                    onAction(OnboardingPreferencesAction.OnStartClicked)
                }
            }
        }
    }
}

@Composable
private fun ExampleFormat(
    modifier: Modifier,
    uiState: OnboardingPreferencesViewState
) {
    Box(
        modifier = Modifier
            .padding(top = 24.dp)
            .fillMaxWidth()
            .shadow(elevation = 1.dp, RoundedCornerShape(16.dp))
            .clip(RoundedCornerShape(16.dp))
            .background(color = MaterialTheme.colorScheme.surfaceContainerLowest)
            .padding(vertical = 24.dp)
    ) {
        Column(
            modifier = Modifier.align(Alignment.Center)
        ) {
            Text(
                modifier = modifier.fillMaxWidth(),
                text = uiState.exampleFormat,
                style = MaterialTheme.typography.headlineLarge,
                textAlign = TextAlign.Center
            )

            Text(
                modifier = modifier
                    .padding(top = 8.dp)
                    .fillMaxWidth(),
                text = stringResource(R.string.onboarding_preferences_money_sent_this_month),
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
@Preview
fun PreviewOnboardingPreferencesScreen() {
    SpendLessFinanceTrackerTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            OnboardingPreferencesScreen(
                modifier = Modifier,
                uiState = OnboardingPreferencesViewState(),
                snackBarHostState = SnackbarHostState(),
                onAction = {

                }
            )
        }
    }
}

