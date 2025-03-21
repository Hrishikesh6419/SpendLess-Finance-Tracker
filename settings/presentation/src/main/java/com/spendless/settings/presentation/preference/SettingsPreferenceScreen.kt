package com.spendless.settings.presentation.preference

import android.content.Context
import android.widget.Toast
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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import com.hrishi.core.domain.model.Currency
import com.hrishi.core.domain.model.DecimalSeparator
import com.hrishi.core.domain.model.ExpenseFormat
import com.hrishi.core.domain.model.ThousandsSeparator
import com.hrishi.core.presentation.designsystem.SpendLessFinanceTrackerTheme
import com.hrishi.core.presentation.designsystem.components.CategorySelector
import com.hrishi.core.presentation.designsystem.components.SegmentedSelector
import com.hrishi.core.presentation.designsystem.components.SpendLessScaffold
import com.hrishi.core.presentation.designsystem.components.SpendLessTopBar
import com.hrishi.core.presentation.designsystem.components.buttons.SpendLessButton
import com.hrishi.presentation.ui.LocalAuthActionHandler
import com.hrishi.presentation.ui.ObserveAsEvents
import com.spendless.settings.apresentation.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import org.koin.androidx.compose.koinViewModel
import java.math.BigDecimal

@Composable
fun SettingsPreferenceScreenRoot(
    modifier: Modifier = Modifier,
    viewModel: SettingsPreferenceViewModel = koinViewModel(),
    onNavigateBack: () -> Unit
) {
    val authActionHandler = LocalAuthActionHandler.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    EventHandler(
        events = viewModel.events,
        scope = scope,
        context = context,
        onNavigateBack = onNavigateBack
    )

    SettingsPreferencesScreen(
        modifier = modifier,
        uiState = uiState,
        onAction = { action ->
            when (action) {
                SettingsPreferencesAction.OnSaveClicked -> {
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
private fun EventHandler(
    events: Flow<SettingsPreferencesEvent>,
    scope: CoroutineScope,
    context: Context,
    onNavigateBack: () -> Unit
) {
    ObserveAsEvents(events) { event ->
        when (event) {
            SettingsPreferencesEvent.NavigateBack -> onNavigateBack()
            SettingsPreferencesEvent.PreferencesSaved -> {
                Toast.makeText(
                    context,
                    context.getString(R.string.preferences_saved_successfully),
                    Toast.LENGTH_SHORT
                ).show()
                onNavigateBack()
            }
        }
    }
}

@Composable
private fun SettingsPreferencesScreen(
    modifier: Modifier = Modifier,
    uiState: SettingsPreferencesViewState,
    onAction: (SettingsPreferencesAction) -> Unit
) {
    SpendLessScaffold(
        containerColor = Color.Transparent,
        topBar = {
            SpendLessTopBar(
                modifier = Modifier.padding(horizontal = 8.dp),
                title = stringResource(R.string.preferences),
                titleColor = MaterialTheme.colorScheme.onSurface,
                onStartIconClick = { onAction(SettingsPreferencesAction.OnBackClicked) }
            )
        }
    ) { contentPadding ->
        SettingsPreferencesContent(
            modifier = modifier.padding(contentPadding),
            uiState = uiState,
            onAction = onAction
        )
    }
}

@Composable
private fun SettingsPreferencesContent(
    modifier: Modifier = Modifier,
    uiState: SettingsPreferencesViewState,
    onAction: (SettingsPreferencesAction) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        ExampleFormat(uiState)

        Spacer(modifier = Modifier.height(20.dp))

        SegmentedSelector(
            title = stringResource(R.string.expense_format),
            options = ExpenseFormat.entries.toTypedArray(),
            selectedOption = uiState.expenseFormat,
            onOptionSelected = {
                onAction(SettingsPreferencesAction.OnExpenseFormatUpdate(it))
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
            title = stringResource(R.string.currency),
            selectedOption = uiState.currency,
            options = Currency.entries.toTypedArray(),
            currencyDisplay = { it.symbol },
            currencyTitleDisplay = { it.title },
            onItemSelected = {
                onAction(SettingsPreferencesAction.OnCurrencyUpdate(it))
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        SegmentedSelector(
            title = stringResource(R.string.decimal_separator),
            options = DecimalSeparator.entries.toTypedArray(),
            selectedOption = uiState.decimalSeparator,
            onOptionSelected = {
                onAction(SettingsPreferencesAction.OnDecimalSeparatorUpdate(it))
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
            title = stringResource(R.string.thousand_separator),
            options = ThousandsSeparator.entries.toTypedArray(),
            selectedOption = uiState.thousandsSeparator,
            onOptionSelected = {
                onAction(SettingsPreferencesAction.OnThousandsSeparatorUpdate(it))
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
            isEnabled = uiState.enableSaveButton,
            buttonText = stringResource(R.string.save)
        ) {
            onAction(SettingsPreferencesAction.OnSaveClicked)
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun ExampleFormat(uiState: SettingsPreferencesViewState) {
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
                text = stringResource(R.string.spend_this_month),
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Preview
@Composable
fun PreviewSettingsPreferenceScreen() {
    SpendLessFinanceTrackerTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            SettingsPreferencesScreen(
                modifier = Modifier,
                uiState = SettingsPreferencesViewState(),
                onAction = {

                }
            )
        }
    }
}