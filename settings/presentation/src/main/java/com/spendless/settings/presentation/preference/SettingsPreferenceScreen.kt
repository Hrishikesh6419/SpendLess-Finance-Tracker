package com.spendless.settings.presentation.preference

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
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
import com.hrishi.core.presentation.designsystem.components.SpendLessTopBar
import com.hrishi.core.presentation.designsystem.components.buttons.SpendLessButton
import com.hrishi.presentation.ui.ObserveAsEvents
import org.koin.androidx.compose.koinViewModel
import java.math.BigDecimal

@Composable
fun SettingsPreferenceScreenRoot(
    modifier: Modifier = Modifier,
    viewModel: SettingsPreferenceViewModel = koinViewModel(),
    onNavigateBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            SettingsPreferencesEvent.NavigateBack -> onNavigateBack()
            SettingsPreferencesEvent.PreferencesSaved -> {
                Toast.makeText(context, "Preferences saved successfully!", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    SettingsPreferencesScreen(
        modifier = modifier,
        uiState = uiState,
        onAction = viewModel::onAction
    )
}

@Composable
fun SettingsPreferencesScreen(
    modifier: Modifier = Modifier,
    uiState: SettingsPreferencesViewState,
    onAction: (SettingsPreferencesAction) -> Unit
) {
    Scaffold(containerColor = Color.Transparent,
        topBar = {
            SpendLessTopBar(
                modifier = Modifier
                    .windowInsetsPadding(WindowInsets.statusBars)
                    .padding(horizontal = 8.dp),
                title = "Preferences",
                titleColor = MaterialTheme.colorScheme.onSurface,
                onStartIconClick = {
                    onAction(SettingsPreferencesAction.OnBackClicked)
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
                ExampleFormat(modifier, uiState)

                SegmentedSelector(
                    modifier = Modifier.padding(top = 20.dp),
                    title = "Expense format",
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

                CategorySelector(
                    modifier = Modifier.padding(top = 16.dp),
                    title = "Currency",
                    selectedOption = uiState.currency,
                    options = Currency.entries.toTypedArray(),
                    currencyDisplay = { it.symbol },
                    currencyTitleDisplay = { it.title },
                    onItemSelected = {
                        onAction(SettingsPreferencesAction.OnCurrencyUpdate(it))
                    }
                )

                SegmentedSelector(
                    modifier = Modifier.padding(top = 16.dp),
                    title = "Decimal separator",
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

                SegmentedSelector(
                    modifier = Modifier.padding(top = 16.dp),
                    title = "Thousand separator",
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

                SpendLessButton(
                    isEnabled = uiState.enableSaveButton,
                    modifier = Modifier.padding(vertical = 34.dp),
                    buttonText = "Save"
                ) {
                    onAction(SettingsPreferencesAction.OnSaveClicked)
                }
            }
        }
    }
}

@Composable
private fun ExampleFormat(
    modifier: Modifier,
    uiState: SettingsPreferencesViewState
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
                text = "spend this month",
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