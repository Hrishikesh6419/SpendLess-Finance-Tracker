package com.spendless.dashboard.domain.usecases.dashboard

import com.hrishi.core.domain.preference.model.UserPreferences
import com.hrishi.core.domain.preference.usecase.PreferenceUseCase
import com.hrishi.core.domain.transactions.model.Transaction
import com.hrishi.core.domain.transactions.usecases.TransactionUseCases
import com.hrishi.core.domain.utils.DataError
import com.hrishi.core.domain.utils.Result
import com.spendless.session_management.domain.model.SessionData
import com.spendless.session_management.domain.usecases.SessionUseCases
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class GetAllTransactionsDataUseCase(
    private val sessionUseCases: SessionUseCases,
    private val preferenceUseCase: PreferenceUseCase,
    private val transactionUseCases: TransactionUseCases
) {
    operator fun invoke(): Flow<Result<AllTransactionsData, DataError>> = flow {
        val sessionData = sessionUseCases.getSessionDataUseCase().first()

        val preferenceFlow = preferenceUseCase.getPreferencesUseCase(sessionData.userId)
        val transactionFlow = transactionUseCases.getTransactionsForUserUseCase(sessionData.userId)

        combine(preferenceFlow, transactionFlow) { preferencesResult, transactionsResult ->
            AllTransactionsCombinedResult(sessionData, preferencesResult, transactionsResult)
        }.collect { result ->
            if (result.isAllSuccess()) {
                emit(Result.Success(result.toAllTransactionsData()))
            } else {
                emit(Result.Error(DataError.Local.UNKNOWN_DATABASE_ERROR))
            }
        }
    }.flowOn(Dispatchers.IO)
}

data class AllTransactionsCombinedResult(
    val sessionData: SessionData,
    val preferencesResult: Result<UserPreferences, DataError>,
    val transactionsResult: Result<List<Transaction>, DataError>
) {
    fun isAllSuccess(): Boolean {
        return preferencesResult is Result.Success &&
                transactionsResult is Result.Success
    }

    fun toAllTransactionsData(): AllTransactionsData {
        return AllTransactionsData(
            preference = (preferencesResult as Result.Success).data,
            transactions = (transactionsResult as Result.Success).data
        )
    }
}

data class AllTransactionsData(
    val preference: UserPreferences,
    val transactions: List<Transaction>
)