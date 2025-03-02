package com.spendless.dashboard.presentation.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hrishi.core.domain.formatting.NumberFormatter
import com.hrishi.core.domain.model.TransactionCategory
import com.hrishi.core.domain.preference.model.UserPreferences
import com.hrishi.core.domain.preference.usecase.SettingsPreferenceUseCase
import com.hrishi.core.domain.transactions.model.Transaction
import com.hrishi.core.domain.transactions.usecases.TransactionUseCases
import com.hrishi.core.domain.utils.CombinedResult
import com.hrishi.core.domain.utils.DataError
import com.hrishi.core.domain.utils.Result
import com.hrishi.core.domain.utils.toShortDateString
import com.spendless.dashboard.presentation.mapper.toTransactionCategoryUI
import com.spendless.dashboard.presentation.mapper.toTransactionUiItem
import com.spendless.session_management.domain.model.SessionData
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
            },
            sessionUseCases.getSessionDataUseCase().flatMapLatest { sessionData ->
                transactionUseCases.getMostPopularExpenseCategoryUseCase(sessionData.userId)
            },
            sessionUseCases.getSessionDataUseCase().flatMapLatest { sessionData ->
                transactionUseCases.getLargestTransactionUseCase(sessionData.userId)
            }
        ) { array: Array<Any?> ->
            CombinedResult(
                array[0] as SessionData,
                array[1] as Result<UserPreferences, DataError>,
                array[2] as Result<List<Transaction>, DataError>,
                array[3] as Result<BigDecimal, DataError>,
                array[4] as Result<TransactionCategory?, DataError>,
                array[5] as Result<Transaction?, DataError>
            )
        }.onEach { (
                       sessionData: SessionData,
                       preferenceResult: Result<UserPreferences, DataError>,
                       transactionResult: Result<List<Transaction>, DataError>,
                       balanceResult: Result<BigDecimal, DataError>,
                       popularCategoryResult: Result<TransactionCategory?, DataError>,
                       largestTransactionResult: Result<Transaction?, DataError>,
                   ) ->
            if (preferenceResult is Result.Success &&
                transactionResult is Result.Success &&
                balanceResult is Result.Success &&
                popularCategoryResult is Result.Success &&
                largestTransactionResult is Result.Success
            ) {
                preference = preferenceResult.data
                _uiState.update { currentState ->
                    currentState.copy(
                        preference = preferenceResult.data,
                        username = sessionData.userName,
                        accountBalance = formatAmount(balanceResult.data),
                        mostPopularCategory = popularCategoryResult.data?.toTransactionCategoryUI(),
                        largestTransaction = largestTransactionResult.data.toLargestTransactionItem(),
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

    private fun Transaction?.toLargestTransactionItem(): LargestTransaction? {
        return this?.let {
            LargestTransaction(
                name = this.transactionName,
                amount = formatAmount(this.amount),
                date = this.transactionDate.toShortDateString()
            )
        }
    }
}