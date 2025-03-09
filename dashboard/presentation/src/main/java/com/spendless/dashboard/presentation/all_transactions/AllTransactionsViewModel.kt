package com.spendless.dashboard.presentation.all_transactions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hrishi.core.domain.preference.usecase.SettingsPreferenceUseCase
import com.hrishi.core.domain.transactions.model.Transaction
import com.hrishi.core.domain.transactions.usecases.TransactionUseCases
import com.hrishi.core.domain.utils.Result
import com.spendless.dashboard.presentation.dashboard.TransactionGroupUIItem
import com.spendless.dashboard.presentation.mapper.toUIItem
import com.spendless.session_management.domain.usecases.SessionUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AllTransactionsViewModel(
    sessionUseCases: SessionUseCase,
    private val sessionPreferenceUseCase: SettingsPreferenceUseCase,
    private val transactionUseCases: TransactionUseCases
) : ViewModel() {

    private val _uiState = MutableStateFlow(AllTransactionsViewState())
    val uiState = _uiState.asStateFlow()

    private val eventChannel = Channel<AllTransactionsEvent>()
    val events = eventChannel.receiveAsFlow()

    fun onAction(action: AllTransactionsAction) {
        when (action) {
            is AllTransactionsAction.OnCardClicked -> {
                _uiState.update { currentState ->
                    currentState.copy(
                        transactions = currentState.transactions?.map { group ->
                            group.copy(
                                transactions = group.transactions.map { transaction ->
                                    if (transaction.transactionId == action.transactionId) {
                                        transaction.copy(isCollapsed = !transaction.isCollapsed)
                                    } else transaction
                                }
                            )
                        }
                    )
                }
            }

            AllTransactionsAction.OnClickBackButton -> {
                viewModelScope.launch {
                    eventChannel.send(AllTransactionsEvent.NavigateBack)
                }
            }

            is AllTransactionsAction.UpdateCreateBottomSheet -> {
                _uiState.update {
                    it.copy(
                        showCreateTransactionsSheet = action.showSheet
                    )
                }
            }

            is AllTransactionsAction.UpdateExportBottomSheet -> {
                _uiState.update {
                    it.copy(
                        showExportTransactionsSheet = action.showSheet
                    )
                }
            }
        }
    }

    init {
        viewModelScope.launch {
            val sessionData = sessionUseCases.getSessionDataUseCase().first()
            combine(
                sessionPreferenceUseCase.getPreferencesUseCase(sessionData.userId),
                transactionUseCases.getTransactionsForUserUseCase(sessionData.userId)
            ) { preferences, transactions ->
                Pair(preferences, transactions)
            }.onEach { (preferencesResult, transactionsResult) ->
                if (
                    preferencesResult is Result.Success &&
                    transactionsResult is Result.Success
                ) {
                    _uiState.update { currentState ->
                        currentState.copy(
                            preference = preferencesResult.data,
                            transactions = groupTransactionsByDate(transactionsResult.data)
                        )
                    }
                }
            }.launchIn(viewModelScope)
        }
    }

    private fun groupTransactionsByDate(transactions: List<Transaction>): List<TransactionGroupUIItem> {
        return transactionUseCases.getTransactionsGroupedByDateUseCase(transactions).map {
            it.toUIItem()
        }
    }
}