package com.spendless.dashboard.presentation.export

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hrishi.core.domain.export.usecases.ExportTransactionsUseCases
import com.hrishi.core.domain.preference.model.UserPreferences
import com.hrishi.core.domain.preference.usecase.SettingsPreferenceUseCase
import com.hrishi.core.domain.transactions.model.Transaction
import com.hrishi.core.domain.transactions.usecases.TransactionUseCases
import com.hrishi.core.domain.utils.Result
import com.spendless.session_management.domain.usecases.SessionUseCases
import kotlinx.coroutines.Dispatchers
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
import kotlinx.coroutines.withContext

class ExportTransactionsViewModel(
    sessionUseCases: SessionUseCases,
    private val sessionPreferenceUseCase: SettingsPreferenceUseCase,
    private val transactionUseCases: TransactionUseCases,
    private val exportTransactionsUseCases: ExportTransactionsUseCases
) : ViewModel() {

    private val _uiState = MutableStateFlow(ExportTransactionsViewState())
    val uiState = _uiState.asStateFlow()

    private val eventChannel = Channel<ExportTransactionsEvent>()
    val events = eventChannel.receiveAsFlow()

    private var preferences: UserPreferences? = null
    private var transactions: List<Transaction>? = null

    private var userId: Long? = 0L

    init {
        combine(
            sessionUseCases.getSessionDataUseCase().flatMapLatest { sessionData ->
                userId = sessionData.userId
                sessionPreferenceUseCase.getPreferencesUseCase(sessionData.userId)
            },
            sessionUseCases.getSessionDataUseCase().flatMapLatest { sessionData ->
                transactionUseCases.getTransactionsForUserUseCase(sessionData.userId)
            }
        ) { preferences, transactions ->
            Pair(preferences, transactions)
        }.onEach { (preferencesResult, transactionsResult) ->
            if (
                preferencesResult is Result.Success &&
                transactionsResult is Result.Success
            ) {
                preferences = preferencesResult.data
                transactions = transactionsResult.data
            }
        }.launchIn(viewModelScope)
    }

    fun onAction(action: ExportTransactionsAction) {
        when (action) {
            ExportTransactionsAction.OnDismissClicked -> {
                viewModelScope.launch {
                    eventChannel.send(ExportTransactionsEvent.CloseBottomSheet)
                }
            }

            ExportTransactionsAction.OnExportClicked -> {
                viewModelScope.launch {
                    withContext(Dispatchers.IO) {
                        val exportResult = exportTransactionsUseCases.exportTransactionUseCase(
                            userId = userId ?: 0L,
                            exportType = _uiState.value.exportType,
                        )
                        when (exportResult) {
                            is Result.Error -> eventChannel.send(
                                ExportTransactionsEvent.ExportStatus(
                                    false
                                )
                            )

                            is Result.Success -> eventChannel.send(
                                ExportTransactionsEvent.ExportStatus(
                                    true
                                )
                            )
                        }
                    }
                }
            }

            is ExportTransactionsAction.OnExportTypeUpdated -> {
                _uiState.update {
                    it.copy(
                        exportType = action.exportType
                    )
                }
            }
        }
    }
}