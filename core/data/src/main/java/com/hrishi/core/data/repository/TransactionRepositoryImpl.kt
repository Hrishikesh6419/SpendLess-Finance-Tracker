package com.hrishi.core.data.repository

import com.hrishi.core.domain.model.TransactionCategory
import com.hrishi.core.domain.transactions.data_source.LocalTransactionDataSource
import com.hrishi.core.domain.transactions.model.Transaction
import com.hrishi.core.domain.transactions.repository.TransactionRepository
import com.hrishi.core.domain.utils.DataError
import com.hrishi.core.domain.utils.Result
import kotlinx.coroutines.flow.Flow
import java.math.BigDecimal
import java.time.LocalDateTime

class TransactionRepositoryImpl(
    private val localTransactionDataSource: LocalTransactionDataSource
) : TransactionRepository {

    override suspend fun insertTransaction(transaction: Transaction): Result<Unit, DataError> {
        return localTransactionDataSource.upsertTransaction(transaction)
    }

    override fun getTransactionsForUser(userId: Long): Flow<Result<List<Transaction>, DataError>> {
        return localTransactionDataSource.getTransactionsForUser(userId)
    }

    override fun getRecurringTransactionSeries(recurringId: Long): Flow<Result<List<Transaction>, DataError>> {
        return localTransactionDataSource.getRecurringTransactionSeries(recurringId)
    }

    override suspend fun getDueRecurringTransactions(currentDate: LocalDateTime): Result<List<Transaction>, DataError> {
        return localTransactionDataSource.getDueRecurringTransactions(currentDate)
    }

    override fun getAccountBalance(userId: Long): Flow<Result<BigDecimal, DataError>> {
        return localTransactionDataSource.getAccountBalance(userId)
    }

    override fun getMostPopularExpenseCategory(userId: Long): Flow<Result<TransactionCategory?, DataError>> {
        return localTransactionDataSource.getMostPopularExpenseCategory(userId)
    }

    override fun getLargestTransaction(userId: Long): Flow<Result<Transaction?, DataError>> {
        return localTransactionDataSource.getLargestTransaction(userId)
    }

    override fun getPreviousWeekTotal(userId: Long): Flow<Result<BigDecimal, DataError>> {
        return localTransactionDataSource.getPreviousWeekTotal(userId)
    }
}