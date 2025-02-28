package com.spendless.dashboard.presentation.create_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hrishi.core.presentation.designsystem.model.ExpenseCategoryTypeUI
import com.hrishi.core.presentation.designsystem.model.RecurringTypeUI
import com.hrishi.core.presentation.designsystem.model.TransactionTypeUI
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.math.BigDecimal

class CreateTransactionViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(initialUiState())
    val uiState: StateFlow<CreateTransactionViewState> = _uiState

    private val eventChannel = Channel<CreateTransactionEvent>(Channel.BUFFERED)
    val events = eventChannel.receiveAsFlow()

    private fun initialUiState(): CreateTransactionViewState {
        val transactionType = TransactionTypeUI.EXPENSE
        return CreateTransactionViewState(
            transactionType = transactionType,
            transactionName = "",
            transactionNameHint = getTransactionHint(transactionType),
            amount = BigDecimal.ZERO,
            noteHint = "Add Note",
            note = "",
            categoryType = ExpenseCategoryTypeUI.OTHER,
            recurringType = RecurringTypeUI.ONE_TIME,
            isCreateButtonEnabled = false
        )
    }

    private fun getTransactionHint(type: TransactionTypeUI): String =
        if (type == TransactionTypeUI.EXPENSE) "Receiver" else "Sender"

    fun onAction(action: CreateTransactionAction) {
        when (action) {
            is CreateTransactionAction.OnCategoryUpdated -> updateState { copy(categoryType = action.category) }
            is CreateTransactionAction.OnFrequencyUpdated -> updateState { copy(recurringType = action.frequency) }
            is CreateTransactionAction.OnTransactionTypeChanged -> updateState {
                copy(
                    transactionType = action.transactionType,
                    transactionNameHint = getTransactionHint(action.transactionType)
                )
            }
            is CreateTransactionAction.OnTransactionNameUpdated -> updateState { copy(transactionName = action.transactionName) }
            is CreateTransactionAction.OnAmountUpdated -> updateState { copy(amount = action.amount) }
            is CreateTransactionAction.OnNoteUpdated -> updateState { copy(note = action.note) }
            CreateTransactionAction.OnCreateClicked -> handleCreateTransaction()
            CreateTransactionAction.OnBottomSheetCloseClicked -> sendEvent(CreateTransactionEvent.CloseBottomSheet)
        }
    }

    private fun updateState(update: CreateTransactionViewState.() -> CreateTransactionViewState) {
        _uiState.update { current ->
            val updated = current.update()
            updated.copy(isCreateButtonEnabled = validateInput(updated))
        }
    }

    private fun validateInput(state: CreateTransactionViewState): Boolean {
        return state.transactionName.isNotBlank() && state.transactionName.length in 3..14 &&
                state.amount > BigDecimal.ZERO
    }

    private fun sendEvent(event: CreateTransactionEvent) {
        viewModelScope.launch {
            eventChannel.send(event)
        }
    }

    private fun handleCreateTransaction() {
    }
}