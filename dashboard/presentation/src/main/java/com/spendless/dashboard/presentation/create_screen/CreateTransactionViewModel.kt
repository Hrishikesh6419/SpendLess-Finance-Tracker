package com.spendless.dashboard.presentation.create_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hrishi.core.presentation.designsystem.model.ExpenseCategoryTypeUI
import com.hrishi.core.presentation.designsystem.model.RecurringTypeUI
import com.hrishi.core.presentation.designsystem.model.TransactionTypeUI
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
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
            noteHint = "Add Note",
            note = "",
            categoryType = ExpenseCategoryTypeUI.OTHER,
            recurringType = RecurringTypeUI.ONE_TIME,
            isCreateButtonEnabled = false
        )
    }

    fun onAction(action: CreateTransactionAction) {
        when (action) {
            CreateTransactionAction.OnCreateClicked -> Unit
            is CreateTransactionAction.OnCategoryUpdated -> {
                _uiState.update {
                    it.copy(
                        categoryType = action.category
                    )
                }
            }

            is CreateTransactionAction.OnFrequencyUpdated -> {
                _uiState.update {
                    it.copy(
                        recurringType = action.frequency
                    )
                }
            }
            is CreateTransactionAction.OnTransactionTypeChanged -> {
                _uiState.update {
                    it.copy(
                        transactionType = action.transactionType
                    )
                }
            }
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

            is CreateTransactionAction.OnNoteUpdated -> {
                _uiState.update {
                    it.copy(
                        note = action.note
                    )
                }
            }

            CreateTransactionAction.OnBottomSheetCloseClicked -> {
                viewModelScope.launch {
                    eventChannel.send(
                        CreateTransactionEvent.CloseBottomSheet
                    )
                }
            }
        }
    }
}