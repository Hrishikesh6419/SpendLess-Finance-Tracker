package com.spendless.dashboard.presentation.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hrishi.core.domain.formatting.NumberFormatter
import com.hrishi.core.domain.preference.model.UserPreferences
import com.hrishi.core.domain.preference.usecase.SettingsPreferenceUseCase
import com.hrishi.core.domain.transactions.usecases.TransactionUseCases
import com.hrishi.core.domain.utils.CombinedResult
import com.hrishi.core.domain.utils.Result
import com.spendless.dashboard.presentation.mapper.toTransactionUiItem
import com.spendless.session_management.domain.usecases.SessionUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

@ExperimentalCoroutinesApi
class DashboardViewModel(
    private val sessionUseCases: SessionUseCase,
    private val sessionPreferenceUseCase: SettingsPreferenceUseCase,
    private val transactionUseCases: TransactionUseCases
) : ViewModel() {

    private val _uiState = MutableStateFlow(DashboardViewState())
    val uiState = _uiState.asStateFlow()

    private val eventChannel = Channel<DashboardEvent>()
    val events = eventChannel.receiveAsFlow()

    private var preference: UserPreferences? = null

    init {
        fetchTransactions()
    }

    private fun fetchTransactions() {
        combine(
            sessionUseCases.getSessionDataUseCase(),
            sessionUseCases.getSessionDataUseCase().flatMapLatest { sessionData ->
                sessionPreferenceUseCase.getPreferencesUseCase(sessionData.userId)
            },
            sessionUseCases.getSessionDataUseCase().flatMapLatest { sessionData ->
                transactionUseCases.getTransactionsForUserUseCase(sessionData.userId)
            },
            sessionUseCases.getSessionDataUseCase().flatMapLatest { sessionData ->
                transactionUseCases.getAccountBalanceUseCase(sessionData.userId)
            }
        ) { sessionData, preferenceResult, transactionResult, balanceResult ->
            CombinedResult(sessionData, preferenceResult, transactionResult, balanceResult)
        }.onEach { (sessionData, preferenceResult, transactionResult, balanceResult) ->
            if (preferenceResult is Result.Success &&
                transactionResult is Result.Success &&
                balanceResult is Result.Success
            ) {
                preference = preferenceResult.data
                _uiState.update { currentState ->
                    currentState.copy(
                        preference = preferenceResult.data,
                        username = sessionData.userName,
                        accountBalance = formatAmount(balanceResult.data),
                        transactions = groupTransactionsByDate(transactionResult.data.map { it.toTransactionUiItem() })
                    )
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

    private fun groupTransactionsByDate(transactions: List<TransactionUIItem>): List<TransactionGroupUIItem> {
        val today = LocalDateTime.now().toLocalDate()
        val yesterday = today.minusDays(1)
        val dateFormatter = DateTimeFormatter.ofPattern("MMMM d")

        return transactions
            .sortedByDescending { it.date }
            .groupBy { transaction ->
                when (transaction.date.toLocalDate()) {
                    today -> "TODAY"
                    yesterday -> "YESTERDAY"
                    else -> transaction.date.format(dateFormatter).uppercase(Locale.US)
                }
            }
            .map { (dateLabel, transactions) ->
                TransactionGroupUIItem(
                    dateLabel = dateLabel,
                    transactions = transactions
                )
            }
    }

    private fun formatAmount(amount: BigDecimal): String {
        return preference?.let {
            NumberFormatter.formatAmount(
                amount = amount,
                expenseFormat = it.expenseFormat,
                decimalSeparator = it.decimalSeparator,
                thousandsSeparator = it.thousandsSeparator,
                currency = it.currency
            )
        } ?: ""
    }
}