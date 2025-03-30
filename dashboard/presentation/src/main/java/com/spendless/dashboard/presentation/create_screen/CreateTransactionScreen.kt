package com.spendless.dashboard.presentation.create_screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hrishi.core.domain.model.Currency
import com.hrishi.core.presentation.designsystem.CloseIcon
import com.hrishi.core.presentation.designsystem.PlusIcon
import com.hrishi.core.presentation.designsystem.SpendLessFinanceTrackerTheme
import com.hrishi.core.presentation.designsystem.components.CategorySelector
import com.hrishi.core.presentation.designsystem.components.SegmentedSelector
import com.hrishi.core.presentation.designsystem.components.buttons.SpendLessButton
import com.hrishi.core.presentation.designsystem.components.text_field.BasicTransactionField
import com.hrishi.core.presentation.designsystem.components.text_field.DecimalSeparatorUI
import com.hrishi.core.presentation.designsystem.components.text_field.ThousandsSeparatorUI
import com.hrishi.core.presentation.designsystem.components.text_field.TransactionTextField
import com.hrishi.core.presentation.designsystem.model.RecurringTypeUI
import com.hrishi.core.presentation.designsystem.model.TransactionCategoryTypeUI
import com.hrishi.core.presentation.designsystem.model.TransactionTypeUI
import com.hrishi.presentation.ui.LocalAuthActionHandler
import com.hrishi.presentation.ui.ObserveAsEvents
import com.hrishi.presentation.ui.UpdateDialogStatusBarAppearance
import com.hrishi.presentation.ui.utils.getFormattedTitle
import com.spendless.dashboard.core.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import org.koin.androidx.compose.koinViewModel
import java.math.BigDecimal
import java.time.LocalDateTime
import java.time.Month

@Composable
fun CreateTransactionScreenRoot(
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit,
    viewModel: CreateTransactionViewModel = koinViewModel()
) {
    val authActionHandler = LocalAuthActionHandler.current
    UpdateDialogStatusBarAppearance(isDarkStatusBarIcons = false)
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    EventHandler(
        events = viewModel.events,
        onDismiss = onDismiss
    )

    CreateTransactionScreen(
        modifier = modifier,
        uiState = uiState,
        onAction = { action ->
            when (action) {
                CreateTransactionAction.OnCreateClicked -> {
                    authActionHandler?.invoke { viewModel.onAction(action) }
                }

                else -> viewModel.onAction(action)
            }
        }
    )
}

@Composable
private fun EventHandler(
    events: Flow<CreateTransactionEvent>,
    onDismiss: () -> Unit
) {
    ObserveAsEvents(events) { event ->
        when (event) {
            CreateTransactionEvent.CloseBottomSheet -> onDismiss()
        }
    }
}

@Composable
private fun CreateTransactionScreen(
    modifier: Modifier = Modifier,
    uiState: CreateTransactionViewState,
    onAction: (CreateTransactionAction) -> Unit
) {
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        delay(500L)
        focusRequester.requestFocus()
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(22.dp))

        CreateTransactionHeader(onAction)

        Spacer(modifier = Modifier.height(28.dp))

        TransactionTypeSelector(uiState, onAction)

        Spacer(modifier = Modifier.height(34.dp))

        TransactionFields(uiState, onAction, focusRequester)

        Spacer(modifier = Modifier.height(12.dp))

        SpendLessButton(
            buttonText = stringResource(R.string.common_create_text),
            onClick = { onAction(CreateTransactionAction.OnCreateClicked) },
            isEnabled = uiState.isCreateButtonEnabled
        )
    }
}

@Composable
private fun CreateTransactionHeader(
    onAction: (CreateTransactionAction) -> Unit
) {
    Row(
        modifier = Modifier
            .height(IntrinsicSize.Max)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = stringResource(R.string.create_transaction),
            style = MaterialTheme.typography.titleLarge
        )
        Icon(
            modifier = Modifier.clickable {
                onAction(CreateTransactionAction.OnBottomSheetCloseClicked)
            },
            tint = Color.Unspecified,
            imageVector = CloseIcon,
            contentDescription = ""
        )
    }
}

@Composable
private fun TransactionTypeSelector(
    uiState: CreateTransactionViewState,
    onAction: (CreateTransactionAction) -> Unit
) {
    SegmentedSelector(
        options = TransactionTypeUI.entries.toTypedArray(),
        selectedOption = uiState.transactionType,
        onOptionSelected = { onAction(CreateTransactionAction.OnTransactionTypeChanged(it)) },
        displayText = { it.displayText() },
        displayIcon = { it.iconRes },
        textSelectedColor = MaterialTheme.colorScheme.primary
    )
}

@Composable
private fun ColumnScope.TransactionFields(
    uiState: CreateTransactionViewState,
    onAction: (CreateTransactionAction) -> Unit,
    focusRequester: FocusRequester
) {
    BasicTransactionField(
        modifier = Modifier
            .fillMaxWidth()
            .focusable()
            .focusRequester(focusRequester)
            .align(Alignment.CenterHorizontally),
        value = uiState.transactionName,
        hint = uiState.transactionNameHint,
        onValueChange = { onAction(CreateTransactionAction.OnTransactionNameUpdated(it)) }
    )

    Spacer(modifier = Modifier.height(24.dp))

    TransactionTextField(
        modifier = Modifier.align(Alignment.CenterHorizontally),
        value = uiState.amount,
        currencySymbol = uiState.currency?.symbol ?: Currency.USD.symbol,
        expenseFormat = uiState.expenseFormat,
        decimalSeparator = uiState.decimalSeparatorUI ?: DecimalSeparatorUI.DOT,
        thousandSeparator = uiState.thousandsSeparatorUI ?: ThousandsSeparatorUI.COMMA,
        onValueChange = { onAction(CreateTransactionAction.OnAmountUpdated(it)) },
        isExpense = uiState.transactionType == TransactionTypeUI.EXPENSE
    )

    Spacer(modifier = Modifier.height(22.dp))

    BasicTransactionField(
        modifier = Modifier.align(Alignment.CenterHorizontally),
        value = uiState.note,
        hint = uiState.noteHint,
        icon = PlusIcon,
        emptyStateStyle = MaterialTheme.typography.bodyMedium.copy(
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.60f)
        ),
        nonEmptyStateStyle = MaterialTheme.typography.bodyMedium.copy(
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.60f)
        ),
        onValueChange = { onAction(CreateTransactionAction.OnNoteUpdated(it)) }
    )

    Spacer(modifier = Modifier.height(32.dp))

    if (uiState.showExpenseCategoryType) {
        CategorySelector(
            modifier = Modifier.fillMaxWidth(),
            showIconBackground = true,
            fontStyle = MaterialTheme.typography.labelMedium,
            selectedOption = uiState.transactionCategoryType,
            options = TransactionCategoryTypeUI.expenseCategories(),
            currencyDisplay = { it.symbol },
            currencyTitleDisplay = { it.title },
            onItemSelected = { onAction(CreateTransactionAction.OnTransactionCategoryUpdated(it)) }
        )
    }

    Spacer(modifier = Modifier.height(8.dp))

    CategorySelector(
        modifier = Modifier.fillMaxWidth(),
        showIconBackground = true,
        iconBackgroundColor = MaterialTheme.colorScheme.tertiaryContainer,
        fontStyle = MaterialTheme.typography.labelMedium,
        selectedOption = uiState.recurringType,
        showMenuIcon = false,
        options = RecurringTypeUI.entries.toTypedArray(),
        currencyDisplay = { it.symbol },
        currencyTitleDisplay = { it.getFormattedTitle(uiState.currentTime) },
        onItemSelected = { onAction(CreateTransactionAction.OnFrequencyUpdated(it)) }
    )
}

@Preview
@Composable
private fun PreviewCreateTransactionScreenRoot() {
    SpendLessFinanceTrackerTheme {
        Surface(
            color = MaterialTheme.colorScheme.surfaceContainerLow
        ) {
            CreateTransactionScreen(
                uiState = CreateTransactionViewState(
                    transactionType = TransactionTypeUI.EXPENSE,
                    transactionName = "",
                    transactionNameHint = "Receiver",
                    amount = BigDecimal.ZERO,
                    noteHint = "Add Note",
                    note = "",
                    transactionCategoryType = TransactionCategoryTypeUI.OTHER,
                    showExpenseCategoryType = true,
                    recurringType = RecurringTypeUI.ONE_TIME,
                    isCreateButtonEnabled = false,
                    currentTime = LocalDateTime.of(2025, Month.JANUARY, 15, 10, 0)
                ),
                onAction = { }
            )
        }
    }
}