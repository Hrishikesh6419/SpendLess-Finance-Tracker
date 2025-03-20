package com.spendless.dashboard.presentation.dashboard

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.hrishi.core.domain.formatting.NumberFormatter
import com.hrishi.core.domain.preference.model.UserPreferences
import com.hrishi.core.domain.transactions.model.Transaction
import com.hrishi.core.domain.transactions.usecases.TransactionUseCases
import com.hrishi.core.domain.utils.Result
import com.hrishi.core.domain.utils.toShortDateString
import com.hrishi.presentation.ui.navigation.DashboardScreenRoute
import com.spendless.dashboard.domain.usecases.dashboard.GetDashboardDataUseCase
import com.spendless.dashboard.presentation.mapper.toTransactionCategoryUI
import com.spendless.dashboard.presentation.mapper.toUIItem
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
class DashboardViewModel(
    savedStateHandle: SavedStateHandle,
    private val getDashboardDataUseCase: GetDashboardDataUseCase,
    private val transactionUseCases: TransactionUseCases
) : ViewModel() {

    private val _uiState = MutableStateFlow(DashboardViewState())
    val uiState = _uiState.asStateFlow()

    private val eventChannel = Channel<DashboardEvent>()
    val events = eventChannel.receiveAsFlow()

    private var preference: UserPreferences? = null

    private var isLaunchedFromWidget =
        savedStateHandle.toRoute<DashboardScreenRoute>().isLaunchedFromWidget

    init {
        fetchDashboardData()
    }

    private fun fetchDashboardData() {
        viewModelScope.launch {
            getDashboardDataUseCase().collect { result ->
                if (result is Result.Success) {
                    val data = result.data
                    preference = data.preference
                    _uiState.update { currentState ->
                        currentState.copy(
                            preference = data.preference,
                            username = data.session.userName,
                            accountBalance = NumberFormatter.formatAmount(
                                data.accountBalance,
                                preference
                            ),
                            mostPopularCategory = data.mostPopularCategory?.toTransactionCategoryUI(),
                            largestTransaction = data.largestTransaction.toLargestTransactionItem(),
                            previousWeekTotal = NumberFormatter.formatAmount(
                                data.previousWeekTotal,
                                preference
                            ),
                            transactions = groupTransactionsByDate(data.transactions),
                            showCreateTransactionSheet = isLaunchedFromWidget
                        )
                    }
                    isLaunchedFromWidget = false
                }
            }
        }
    }

    fun onAction(action: DashboardAction) {
        when (action) {
            DashboardAction.NavigationClick -> Unit
            DashboardAction.OnShowAllTransactionsClicked -> emitEvent(DashboardEvent.NavigateToAllTransactions)
            DashboardAction.OnSettingsClicked -> emitEvent(DashboardEvent.NavigateToSettings)
            is DashboardAction.UpdatedBottomSheet -> updateCreateSheetState(action.showSheet)
            is DashboardAction.UpdateExportBottomSheet -> updateExportSheetState(action.showSheet)
            is DashboardAction.OnCardClicked -> toggleTransactionCard(action.transactionId)
        }
    }

    private fun updateCreateSheetState(show: Boolean) {
        _uiState.update { it.copy(showCreateTransactionSheet = show) }
    }

    private fun updateExportSheetState(show: Boolean) {
        _uiState.update { it.copy(showExportTransactionSheet = show) }
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

    private fun emitEvent(event: DashboardEvent) {
        viewModelScope.launch { eventChannel.send(event) }
    }

    private fun Transaction?.toLargestTransactionItem(): LargestTransaction? {
        return this?.let {
            LargestTransaction(
                name = this.transactionName,
                amount = NumberFormatter.formatAmount(this.amount, preference),
                date = this.transactionDate.toShortDateString()
            )
        }
    }
}