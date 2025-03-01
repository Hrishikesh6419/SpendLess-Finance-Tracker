package com.hrishi.core.domain.transactions.data_source

import com.hrishi.core.domain.transactions.model.Transaction
import com.hrishi.core.domain.utils.DataError
import com.hrishi.core.domain.utils.Result
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

interface LocalTransactionDataSource {
    suspend fun upsertTransaction(transaction: Transaction): Result<Unit, DataError>
    fun getTransactionsForUser(userId: Long): Flow<Result<List<Transaction>, DataError>>
    fun getRecurringTransactionSeries(recurringId: Long): Flow<Result<List<Transaction>, DataError>>
    suspend fun getDueRecurringTransactions(currentDate: LocalDateTime): Result<List<Transaction>, DataError>
}