package com.spendless.dashboard.domain.usecases.dashboard

import com.hrishi.core.domain.model.TransactionCategory
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
import java.math.BigDecimal

class GetDashboardDataUseCase(
    private val sessionUseCases: SessionUseCases,
    private val preferenceUseCase: PreferenceUseCase,
    private val transactionUseCases: TransactionUseCases
) {

    operator fun invoke(): Flow<Result<DashboardData, DataError>> = flow {
        val sessionData = sessionUseCases.getSessionDataUseCase().first()

        val preferenceFlow = preferenceUseCase.getPreferencesUseCase(sessionData.userId)
        val transactionFlow = transactionUseCases.getTransactionsForUserUseCase(
            userId = sessionData.userId,
            limit = FETCH_TRANSACTIONS_LIMIT
        )
        val accountBalanceFlow = transactionUseCases.getAccountBalanceUseCase(sessionData.userId)
        val popularCategoryFlow =
            transactionUseCases.getMostPopularExpenseCategoryUseCase(sessionData.userId)
        val largestTransactionFlow =
            transactionUseCases.getLargestTransactionUseCase(sessionData.userId)
        val previousWeekTotalFlow =
            transactionUseCases.getPreviousWeekTotalUseCase(sessionData.userId)

        val firstCombined = combine(
            preferenceFlow,
            transactionFlow,
            accountBalanceFlow,
            popularCategoryFlow
        ) { prefResult, transResult, balanceResult, popCatResult ->
            Quadruple(prefResult, transResult, balanceResult, popCatResult)
        }

        combine(
            firstCombined,
            largestTransactionFlow,
            previousWeekTotalFlow
        ) { (prefResult, transResult, balanceResult, popCatResult), largeTransResult, weekTotalResult ->

            DashboardCombinedResult(
                sessionData = sessionData,
                preferencesResult = prefResult,
                transactionsResult = transResult,
                balanceResult = balanceResult,
                popularCategoryResult = popCatResult,
                largestTransactionResult = largeTransResult,
                previousWeekTotalResult = weekTotalResult
            )
        }.collect { result ->
            if (result.isAllSuccess()) {
                emit(Result.Success(result.toDashboardData()))
            } else {
                emit(Result.Error(DataError.Local.UNKNOWN_DATABASE_ERROR))
            }
        }
    }.flowOn(Dispatchers.IO)

    companion object {
        private const val FETCH_TRANSACTIONS_LIMIT = 20
    }
}

data class DashboardCombinedResult(
    val sessionData: SessionData,
    val preferencesResult: Result<UserPreferences, DataError>,
    val transactionsResult: Result<List<Transaction>, DataError>,
    val balanceResult: Result<BigDecimal, DataError>,
    val popularCategoryResult: Result<TransactionCategory?, DataError>,
    val largestTransactionResult: Result<Transaction?, DataError>,
    val previousWeekTotalResult: Result<BigDecimal, DataError>
) {
    fun isAllSuccess(): Boolean {
        return preferencesResult is Result.Success &&
                transactionsResult is Result.Success &&
                balanceResult is Result.Success &&
                popularCategoryResult is Result.Success &&
                largestTransactionResult is Result.Success &&
                previousWeekTotalResult is Result.Success
    }

    fun toDashboardData(): DashboardData {
        return DashboardData(
            session = sessionData,
            preference = (preferencesResult as Result.Success).data,
            transactions = (transactionsResult as Result.Success).data,
            accountBalance = (balanceResult as Result.Success).data,
            mostPopularCategory = (popularCategoryResult as Result.Success).data,
            largestTransaction = (largestTransactionResult as Result.Success).data,
            previousWeekTotal = (previousWeekTotalResult as Result.Success).data
        )
    }
}

data class DashboardData(
    val session: SessionData,
    val preference: UserPreferences,
    val transactions: List<Transaction>,
    val accountBalance: BigDecimal,
    val mostPopularCategory: TransactionCategory?,
    val largestTransaction: Transaction?,
    val previousWeekTotal: BigDecimal
)

data class Quadruple<A, B, C, D>(
    val first: A,
    val second: B,
    val third: C,
    val fourth: D
)