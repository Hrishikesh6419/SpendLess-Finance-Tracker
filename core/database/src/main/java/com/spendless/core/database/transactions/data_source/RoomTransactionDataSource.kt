package com.spendless.core.database.transactions.data_source

import com.hrishi.core.domain.model.RecurringType
import com.hrishi.core.domain.transactions.data_source.LocalTransactionDataSource
import com.hrishi.core.domain.transactions.model.Transaction
import com.hrishi.core.domain.utils.CalendarUtils
import com.hrishi.core.domain.utils.DataError
import com.hrishi.core.domain.utils.Result
import com.spendless.core.database.transactions.dao.TransactionsDao
import com.spendless.core.database.transactions.utils.toTransaction
import com.spendless.core.database.transactions.utils.toTransactionEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.time.LocalDateTime

class RoomTransactionDataSource(
    private val transactionsDao: TransactionsDao
) : LocalTransactionDataSource {

    override suspend fun upsertTransaction(transaction: Transaction): Result<Unit, DataError> {
        return try {
            val transactionEntity = transaction.toTransactionEntity()

            val insertedId = transactionsDao.upsertTransaction(transactionEntity)

            if (transactionEntity.recurringType != RecurringType.ONE_TIME && transactionEntity.recurringTransactionId == null) {
                val updatedEntity = transactionEntity.copy(
                    transactionId = insertedId,
                    recurringTransactionId = insertedId
                )
                transactionsDao.upsertTransaction(updatedEntity)
            }

            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(DataError.Local.UNKNOWN_DATABASE_ERROR)
        }
    }

    override fun getTransactionsForUser(userId: Long): Flow<Result<List<Transaction>, DataError>> {
        return transactionsDao.getTransactionsForUser(userId)
            .map { transactions ->
                if (transactions.isNotEmpty()) {
                    Result.Success(transactions.map { it.toTransaction() })
                } else {
                    Result.Error(DataError.Local.TRANSACTION_FETCH_ERROR)
                }
            }
            .catch {
                emit(Result.Error(DataError.Local.UNKNOWN_DATABASE_ERROR))
            }
    }

    override fun getRecurringTransactionSeries(recurringId: Long): Flow<Result<List<Transaction>, DataError>> {
        return transactionsDao.getRecurringTransactionSeries(recurringId)
            .map { transactions ->
                if (transactions.isNotEmpty()) {
                    Result.Success(transactions.map { it.toTransaction() })
                } else {
                    Result.Error(DataError.Local.TRANSACTION_FETCH_ERROR)
                }
            }
            .catch {
                emit(Result.Error(DataError.Local.UNKNOWN_DATABASE_ERROR))
            }
    }

    override suspend fun getDueRecurringTransactions(currentDate: LocalDateTime): Result<List<Transaction>, DataError> {
        return try {
            val currentDateMillis = CalendarUtils.toEpochMillis(currentDate)

            val transactions = transactionsDao.getDueRecurringTransactions(currentDateMillis)
            if (transactions.isNotEmpty()) {
                Result.Success(transactions.map { it.toTransaction() })
            } else {
                Result.Error(DataError.Local.TRANSACTION_FETCH_ERROR)
            }
        } catch (e: Exception) {
            Result.Error(DataError.Local.UNKNOWN_DATABASE_ERROR)
        }
    }
}