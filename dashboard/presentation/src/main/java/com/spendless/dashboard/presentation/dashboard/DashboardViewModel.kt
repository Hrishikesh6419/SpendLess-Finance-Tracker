package com.spendless.dashboard.presentation.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hrishi.core.domain.transactions.model.Transaction
import com.hrishi.core.domain.transactions.usecases.TransactionUseCases
import com.hrishi.core.domain.utils.Result
import com.spendless.dashboard.presentation.mapper.toTransactionUiItem
import com.spendless.session_management.domain.usecases.SessionUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DashboardViewModel(
    private val sessionUseCases: SessionUseCase,
    private val transactionUseCases: TransactionUseCases
) : ViewModel() {

    private val _uiState = MutableStateFlow(DashboardViewState())
    val uiState = _uiState.asStateFlow()

    private val eventChannel = Channel<DashboardEvent>()
    val events = eventChannel.receiveAsFlow()

    init {
        fetchTransactions()
    }

    private fun fetchTransactions() {
        sessionUseCases.getSessionDataUseCase()
            .onEach { sessionData ->
                transactionUseCases.getTransactionsForUserUseCase(sessionData.userId)
                    .collect { result ->
                        when (result) {
                            is Result.Error -> Unit
                            is Result.Success -> {
                                val transactions: List<Transaction> = result.data
                                _uiState.update {
                                    it.copy(
                                        username = sessionData.userName,
                                        accountBalance = "$1234",
                                        transactions = transactions.map { transaction ->
                                            transaction.toTransactionUiItem()
                                        }
                                    )
                                }
                            }
                        }
                    }
            }.launchIn(viewModelScope)
    }

    fun onAction(action: DashboardAction) {
        when (action) {
            DashboardAction.NavigationClick -> Unit
            DashboardAction.OnSettingsClicked -> {
                viewModelScope.launch {
                    eventChannel.send(DashboardEvent.NavigateToSettings)
                }
            }
            is DashboardAction.UpdatedBottomSheet -> {
                _uiState.update {
                    it.copy(
                        showCreateTransactionSheet = action.showSheet
                    )
                }
            }
        }
    }
}