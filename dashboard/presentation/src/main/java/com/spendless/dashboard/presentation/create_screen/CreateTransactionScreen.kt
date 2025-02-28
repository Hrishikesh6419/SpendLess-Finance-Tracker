package com.spendless.dashboard.presentation.create_screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
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
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hrishi.core.presentation.designsystem.CloseIcon
import com.hrishi.core.presentation.designsystem.PlusIcon
import com.hrishi.core.presentation.designsystem.SpendLessFinanceTrackerTheme
import com.hrishi.core.presentation.designsystem.components.CategorySelector
import com.hrishi.core.presentation.designsystem.components.SegmentedSelector
import com.hrishi.core.presentation.designsystem.components.buttons.SpendLessButton
import com.hrishi.core.presentation.designsystem.components.text_field.BasicTransactionField
import com.hrishi.core.presentation.designsystem.components.text_field.TransactionTextField
import com.hrishi.core.presentation.designsystem.model.ExpenseCategoryTypeUI
import com.hrishi.core.presentation.designsystem.model.RecurringTypeUI
import com.hrishi.core.presentation.designsystem.model.TransactionTypeUI
import com.hrishi.presentation.ui.ObserveAsEvents
import org.koin.androidx.compose.koinViewModel
import java.math.BigDecimal

@Composable
fun CreateTransactionScreenRoot(
    modifier: Modifier = Modifier,
    viewModel: CreateTransactionViewModel = koinViewModel()
) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackBarHostState = remember { SnackbarHostState() }
    val keyboardController = LocalSoftwareKeyboardController.current

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {

            else -> {}
        }
    }

    CreateTransactionScreen(
        modifier = modifier,
        uiState = uiState,
        snackbarHostState = snackBarHostState,
        onAction = viewModel::onAction
    )
}

@Composable
private fun CreateTransactionScreen(
    modifier: Modifier = Modifier,
    uiState: CreateTransactionViewState,
    snackbarHostState: SnackbarHostState,
    onAction: (CreateTransactionAction) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {

        Spacer(modifier = Modifier.height(22.dp))

        Row(
            modifier = Modifier
                .height(IntrinsicSize.Max)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Create Transaction",
                style = MaterialTheme.typography.titleLarge
            )

            Icon(
                tint = Color.Unspecified,
                imageVector = CloseIcon,
                contentDescription = ""
            )
        }

        Spacer(modifier = Modifier.height(28.dp))

        SegmentedSelector(
            options = TransactionTypeUI.entries.toTypedArray(),
            selectedOption = uiState.transactionType,
            onOptionSelected = {
                onAction(CreateTransactionAction.OnTransactionTypeChanged(it))
            },
            displayText = {
                it.displayText()
            },
            displayIcon = { it.iconRes },
            textSelectedColor = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(34.dp))

        BasicTransactionField(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally),
            value = uiState.transactionName,
            hint = uiState.transactionNameHint,
            onValueChange = {
                onAction(CreateTransactionAction.OnTransactionNameUpdated(it))
            }
        )

        Spacer(modifier = Modifier.height(24.dp))

        TransactionTextField(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            value = uiState.amount,
            onValueChange = {
                onAction(CreateTransactionAction.OnAmountUpdated(it))
            },
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
            onValueChange = {
                onAction(CreateTransactionAction.OnNoteUpdated(it))
            }
        )

        Spacer(modifier = Modifier.height(32.dp))

        CategorySelector(
            modifier = Modifier
                .fillMaxWidth(),
            showIconBackground = true,
            fontStyle = MaterialTheme.typography.labelMedium,
            selectedOption = uiState.categoryType,
            options = ExpenseCategoryTypeUI.entries.toTypedArray(),
            currencyDisplay = { it.symbol },
            currencyTitleDisplay = { it.title },
            onItemSelected = {
                onAction(CreateTransactionAction.OnCategoryUpdated(it))
            }
        )

        Spacer(modifier = Modifier.height(8.dp))

        CategorySelector(
            modifier = Modifier
                .fillMaxWidth(),
            showIconBackground = true,
            iconBackgroundColor = MaterialTheme.colorScheme.tertiaryContainer,
            fontStyle = MaterialTheme.typography.labelMedium,
            selectedOption = uiState.recurringType,
            showMenuIcon = false,
            options = RecurringTypeUI.entries.toTypedArray(),
            currencyDisplay = { it.symbol },
            currencyTitleDisplay = { it.title },
            onItemSelected = {
                onAction(CreateTransactionAction.OnFrequencyUpdated(it))
            }
        )

        Spacer(modifier = Modifier.height(12.dp))

        SpendLessButton(
            buttonText = "Create",
            onClick = {
                onAction(CreateTransactionAction.OnCreateClicked)
            }
        )
    }
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
                    categoryType = ExpenseCategoryTypeUI.OTHER,
                    recurringType = RecurringTypeUI.ONE_TIME,
                    isCreateButtonEnabled = false
                ),
                snackbarHostState = SnackbarHostState(),
                onAction = {

                }
            )
        }
    }
}