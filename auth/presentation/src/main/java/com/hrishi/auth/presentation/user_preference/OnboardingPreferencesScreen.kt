package com.hrishi.auth.presentation.user_preference

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.hrishi.core.presentation.designsystem.components.CategorySelector
import com.hrishi.core.presentation.designsystem.components.SegmentedSelector
import com.hrishi.core.presentation.designsystem.components.SpendLessScaffold
import com.hrishi.core.presentation.designsystem.components.SpendLessSnackBarHost
import com.hrishi.core.presentation.designsystem.components.SpendLessTopBar
import com.hrishi.core.presentation.designsystem.components.buttons.SpendLessButton
import com.hrishi.presentation.ui.ObserveAsEvents
import com.hrishi.presentation.ui.showTimedSnackBar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import org.koin.androidx.compose.koinViewModel
import java.math.BigDecimal

@Composable
fun OnboardingPreferencesScreenRoot(
    modifier: Modifier = Modifier,
    viewModel: OnboardingPreferencesViewModel = koinViewModel(),
    onNavigateToDashboardScreen: () -> Unit,
    onNavigateBack: () -> Unit,
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackBarHostState = remember { SnackbarHostState() }

    EventHandler(
        events = viewModel.events,
        snackBarHostState = snackBarHostState,
        scope = scope,
        context = context,
        onNavigateToDashboardScreen = onNavigateToDashboardScreen,
        onNavigateBack = onNavigateBack
    )

    OnboardingPreferencesScreen(
        modifier = modifier,
        uiState = uiState,
        snackBarHostState = snackBarHostState,
        onAction = viewModel::onAction
    )
}

@Composable
private fun EventHandler(
    events: Flow<OnboardingPreferencesEvent>,
    snackBarHostState: SnackbarHostState,
    scope: CoroutineScope,
    context: Context,
    onNavigateToDashboardScreen: () -> Unit,
    onNavigateBack: () -> Unit
) {
    ObserveAsEvents(events) { event ->
        when (event) {
            OnboardingPreferencesEvent.NavigateToDashboardScreen -> onNavigateToDashboardScreen()
            OnboardingPreferencesEvent.OnBackClicked -> onNavigateBack()
            is OnboardingPreferencesEvent.Error -> {
                val message = when (event) {
                    OnboardingPreferencesEvent.Error.DuplicateEntry ->
                        context.getString(R.string.common_error_username_taken)

                    OnboardingPreferencesEvent.Error.Generic ->
                        context.getString(R.string.common_error_something_went_wrong)
                }
                scope.showTimedSnackBar(snackBarHostState, message)
            }
        }
    }
}

@Composable
private fun OnboardingPreferencesScreen(
    modifier: Modifier = Modifier,
    uiState: OnboardingPreferencesViewState,
    snackBarHostState: SnackbarHostState,
    onAction: (OnboardingPreferencesAction) -> Unit
) {
    SpendLessScaffold(
        containerColor = Color.Transparent,
        topBar = {
            SpendLessTopBar {
                onAction(OnboardingPreferencesAction.OnBackClicked)
            }
        },
        snackbarHost = { SpendLessSnackBarHost(snackBarHostState) }
    ) { contentPadding ->
        OnboardingPreferencesContent(
            modifier = modifier.padding(contentPadding),
            uiState = uiState,
            onAction = onAction
        )
    }
}

@Composable
private fun OnboardingPreferencesContent(
    modifier: Modifier = Modifier,
    uiState: OnboardingPreferencesViewState,
    onAction: (OnboardingPreferencesAction) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(R.string.onboarding_preferences_headline),
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(R.string.onboarding_preferences_sub_headline),
            style = MaterialTheme.typography.bodyMedium
        )

        ExampleFormat(uiState)

        Spacer(modifier = Modifier.height(20.dp))

        SegmentedSelector(
            title = stringResource(R.string.onboarding_preferences_expenses_format_title),
            options = ExpenseFormat.entries.toTypedArray(),
            selectedOption = uiState.expenseFormat,
            onOptionSelected = {
                onAction(OnboardingPreferencesAction.OnExpenseFormatUpdate(it))
            },
            displayText = {
                it.displayText(
                    number = BigDecimal(-10.00),
                    currency = uiState.currency,
                    keepDecimal = false
                )
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        CategorySelector(
            title = stringResource(R.string.onboarding_preferences_currency_title),
            selectedOption = uiState.currency,
            options = Currency.entries.toTypedArray(),
            currencyDisplay = { it.symbol },
            currencyTitleDisplay = { it.title },
            onItemSelected = {
                onAction(OnboardingPreferencesAction.OnCurrencyUpdate(it))
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        SegmentedSelector(
            title = stringResource(R.string.onboarding_preferences_decimal_separator_title),
            options = DecimalSeparator.entries.toTypedArray(),
            selectedOption = uiState.decimalSeparator,
            onOptionSelected = {
                onAction(OnboardingPreferencesAction.OnDecimalSeparatorUpdate(it))
            },
            displayText = {
                it.displayText(
                    number = BigDecimal(1.00),
                    keepDecimal = true
                )
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        SegmentedSelector(
            title = stringResource(R.string.onboarding_preferences_thousands_separator_title),
            options = ThousandsSeparator.entries.toTypedArray(),
            selectedOption = uiState.thousandsSeparator,
            onOptionSelected = {
                onAction(OnboardingPreferencesAction.OnThousandsSeparatorUpdate(it))
            },
            displayText = {
                it.displayText(
                    number = BigDecimal(1000.00),
                    keepDecimal = false
                )
            }
        )

        Spacer(modifier = Modifier.height(34.dp))

        SpendLessButton(
            isEnabled = uiState.enableStartTracking,
            buttonText = stringResource(R.string.onboarding_preferences_start_button_title)
        ) {
            onAction(OnboardingPreferencesAction.OnStartClicked)
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun ExampleFormat(uiState: OnboardingPreferencesViewState) {
    Box(
        modifier = Modifier
            .padding(top = 24.dp)
            .fillMaxWidth()
            .shadow(elevation = 1.dp, RoundedCornerShape(16.dp))
            .clip(RoundedCornerShape(16.dp))
            .background(color = MaterialTheme.colorScheme.surfaceContainerLowest)
            .padding(vertical = 24.dp)
    ) {
        Column(modifier = Modifier.align(Alignment.Center)) {
            Text(
                text = uiState.exampleFormat,
                style = MaterialTheme.typography.headlineLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Text(
                modifier = Modifier
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
private fun PreviewOnboardingPreferencesScreen() {
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