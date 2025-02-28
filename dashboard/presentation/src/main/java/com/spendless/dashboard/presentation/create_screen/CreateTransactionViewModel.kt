package com.spendless.dashboard.presentation.create_screen

import androidx.lifecycle.ViewModel
import com.hrishi.core.presentation.designsystem.model.CategoryTypeUI
import com.hrishi.core.presentation.designsystem.model.RecurringTypeUI
import com.hrishi.core.presentation.designsystem.model.TransactionTypeUI
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import java.math.BigDecimal

class CreateTransactionViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(initialUiState())
    val uiState = _uiState.asStateFlow()

    private val eventChannel = Channel<CreateTransactionEvent>()
    val events = eventChannel.receiveAsFlow()

    private fun initialUiState(): CreateTransactionViewState {
        val transactionType = TransactionTypeUI.EXPENSE
        return CreateTransactionViewState(
            transactionType = transactionType,
            transactionName = "",
            transactionNameHint = if (transactionType == TransactionTypeUI.EXPENSE) {
                "Receiver"
            } else {
                "Sender"
            },
            amount = BigDecimal.ZERO,
            note = "",
            categoryType = CategoryTypeUI.OTHER,
            recurringType = RecurringTypeUI.ONE_TIME,
            isCreateButtonEnabled = false
        )
    }

    fun onAction(action: CreateTransactionAction) {
        when (action) {
            CreateTransactionAction.OnCategoryClicked -> Unit
            CreateTransactionAction.OnCreateClicked -> Unit
            CreateTransactionAction.OnFrequencyClicked -> Unit
            is CreateTransactionAction.OnTransactionTypeChanged -> Unit
            is CreateTransactionAction.OnTransactionNameUpdated -> {
                _uiState.update {
                    it.copy(
                        transactionName = action.transactionName
                    )
                }
            }

            is CreateTransactionAction.OnAmountUpdated -> {
                _uiState.update {
                    it.copy(
                        amount = action.amount
                    )
                }
            }
        }
    }
}