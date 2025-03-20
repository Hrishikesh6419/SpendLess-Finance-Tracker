package com.spendless.dashboard.presentation.all_transactions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hrishi.core.domain.transactions.model.Transaction
import com.hrishi.core.domain.transactions.usecases.TransactionUseCases
import com.hrishi.core.domain.utils.Result
import com.spendless.dashboard.domain.usecases.dashboard.GetAllTransactionsDataUseCase
import com.spendless.dashboard.presentation.dashboard.TransactionGroupUIItem
import com.spendless.dashboard.presentation.mapper.toUIItem
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AllTransactionsViewModel(
    private val getAllTransactionsDataUseCase: GetAllTransactionsDataUseCase,
    private val transactionUseCases: TransactionUseCases
) : ViewModel() {

    private val _uiState = MutableStateFlow(AllTransactionsViewState())
    val uiState = _uiState.asStateFlow()

    private val eventChannel = Channel<AllTransactionsEvent>()
    val events = eventChannel.receiveAsFlow()

    init {
        fetchTransactionsData()
    }

    private fun fetchTransactionsData() {
        viewModelScope.launch {
            getAllTransactionsDataUseCase().collect { result ->
                if (result is Result.Success) {
                    val data = result.data
                    _uiState.update { currentState ->
                        currentState.copy(
                            preference = data.preference,
                            transactions = groupTransactionsByDate(data.transactions)
                        )
                    }
                }
            }
        }
    }

    fun onAction(action: AllTransactionsAction) {
        when (action) {
            is AllTransactionsAction.OnCardClicked -> toggleTransactionCard(action.transactionId)
            AllTransactionsAction.OnClickBackButton -> emitEvent(AllTransactionsEvent.NavigateBack)
            is AllTransactionsAction.UpdateCreateBottomSheet -> updateCreateSheet(action.showSheet)
            is AllTransactionsAction.UpdateExportBottomSheet -> updateExportSheet(action.showSheet)
        }
    }

    private fun emitEvent(event: AllTransactionsEvent) {
        viewModelScope.launch { eventChannel.send(event) }
    }

    private fun updateCreateSheet(show: Boolean) {
        _uiState.update { it.copy(showCreateTransactionsSheet = show) }
    }

    private fun updateExportSheet(show: Boolean) {
        _uiState.update { it.copy(showExportTransactionsSheet = show) }
    }

    private fun toggleTransactionCard(transactionId: Long?) {
        _uiState.update { currentState ->
            currentState.copy(
                transactions = currentState.transactions?.map { group ->
                    group.copy(
                        transactions = group.transactions.map { transaction ->
                            if (transaction.transactionId == transactionId) {
                                transaction.copy(isCollapsed = !transaction.isCollapsed)
                            } else transaction
                        }
                    )
                }
            )
        }
    }

    private fun groupTransactionsByDate(transactions: List<Transaction>): List<TransactionGroupUIItem> {
        return transactionUseCases.getTransactionsGroupedByDateUseCase(transactions)
            .map { it.toUIItem() }
    }
}