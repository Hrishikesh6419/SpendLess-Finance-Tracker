package com.spendless.core.database.transactions.data_source

import android.util.Log
import com.hrishi.core.domain.model.RecurringType
import com.hrishi.core.domain.model.TransactionCategory
import com.hrishi.core.domain.time.TimeProvider
import com.hrishi.core.domain.transactions.data_source.LocalTransactionDataSource
import com.hrishi.core.domain.transactions.model.Transaction
import com.hrishi.core.domain.utils.DataError
import com.hrishi.core.domain.utils.Result
import com.hrishi.core.domain.utils.getPreviousWeekRange
import com.spendless.core.database.transactions.dao.TransactionsDao
import com.spendless.core.database.transactions.utils.toTransaction
import com.spendless.core.database.transactions.utils.toTransactionEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.math.BigDecimal
import java.time.LocalDateTime
import kotlin.coroutines.cancellation.CancellationException

class RoomTransactionDataSource(
    private val transactionsDao: TransactionsDao,
    private val timeProvider: TimeProvider
) : LocalTransactionDataSource {

    override suspend fun upsertTransaction(transaction: Transaction): Result<Unit, DataError> {
        return try {
            val transactionEntity = transaction.toTransactionEntity()

            val insertedId = withContext(Dispatchers.IO) {
                transactionsDao.upsertTransaction(transactionEntity)
            }

            val decryptedRecurringType = try {
                RecurringType.valueOf(transactionEntity.recurringType)
            } catch (e: Exception) {
                RecurringType.ONE_TIME
            }

            if (decryptedRecurringType != RecurringType.ONE_TIME && transactionEntity.recurringTransactionId == null) {
                withContext(Dispatchers.IO) {
                    val updatedEntity = transactionEntity.copy(
                        transactionId = insertedId,
                        recurringTransactionId = insertedId
                    )
                    transactionsDao.upsertTransaction(updatedEntity)
                }
            }

            Result.Success(Unit)
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            Result.Error(DataError.Local.UNKNOWN_DATABASE_ERROR)
        }
    }

    override fun getTransactionsForUser(
        userId: Long,
        limit: Int?
    ): Flow<Result<List<Transaction>, DataError>> {
        return transactionsDao.getTransactionsForUser(userId, limit)
            .map { transactions ->
                Result.Success(transactions.map { it.toTransaction() }) as Result<List<Transaction>, DataError>
            }
            .catch { e ->
                if (e is CancellationException) throw e
                emit(Result.Error(DataError.Local.UNKNOWN_DATABASE_ERROR))
            }
            .flowOn(Dispatchers.IO)
    }

    override suspend fun getDueRecurringTransactions(currentDate: LocalDateTime): Result<List<Transaction>, DataError> {
        return try {
            val transactions = withContext(Dispatchers.IO) {
                transactionsDao.getDueRecurringTransactions(currentDate)
                    .map { it.toTransaction() }
            }

            val recurringTransactions = transactions.filter { it.recurringType != RecurringType.ONE_TIME }

            if (recurringTransactions.isNotEmpty()) {
                Result.Success(recurringTransactions)
            } else {
                Result.Error(DataError.Local.TRANSACTION_FETCH_ERROR)
            }
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            Result.Error(DataError.Local.UNKNOWN_DATABASE_ERROR)
        }
    }

    override fun getAccountBalance(userId: Long): Flow<Result<BigDecimal, DataError>> {
        return transactionsDao.getAllTransactionAmounts(userId)
            .map { amounts ->
                try {
                    val total = amounts
                        .mapNotNull { it.toBigDecimalOrNull() }
                        .reduceOrNull(BigDecimal::add) ?: BigDecimal.ZERO

                    Result.Success(total)
                } catch (e: Exception) {
                    Result.Error(DataError.Local.UNKNOWN_DATABASE_ERROR)
                }
            }
            .catch { e ->
                if (e is CancellationException) throw e
                emit(Result.Error(DataError.Local.UNKNOWN_DATABASE_ERROR))
            }
            .flowOn(Dispatchers.IO)
    }

    override fun getMostPopularExpenseCategory(userId: Long): Flow<Result<TransactionCategory?, DataError>> {
        return transactionsDao.getExpenseCategories(userId)
            .map { categoriesStrings ->
                val categories = categoriesStrings.mapNotNull { category ->
                    TransactionCategory.entries.find { it.name == category }
                }

                val mostPopularCategory = categories
                    .groupingBy { it }
                    .eachCount()
                    .maxByOrNull { it.value }
                    ?.key

                Result.Success(mostPopularCategory) as Result<TransactionCategory?, DataError>
            }
            .catch { e ->
                if (e is CancellationException) throw e
                emit(Result.Error(DataError.Local.UNKNOWN_DATABASE_ERROR))
            }
            .flowOn(Dispatchers.IO)
    }

    override fun getLargestTransaction(userId: Long): Flow<Result<Transaction?, DataError>> {
        return transactionsDao.getAllExpenses(userId)
            .map { transactions ->
                val decryptedTransactions = transactions.map { it.toTransaction() }
                val largestTransaction = decryptedTransactions.minByOrNull { it.amount }

                Result.Success(largestTransaction) as Result<Transaction?, DataError>
            }
            .catch { e ->
                if (e is CancellationException) throw e
                emit(Result.Error(DataError.Local.UNKNOWN_DATABASE_ERROR))
            }
            .flowOn(Dispatchers.IO)
    }

    override fun getPreviousWeekTotal(userId: Long): Flow<Result<BigDecimal, DataError>> {
        val (startDate, endDate) = timeProvider.currentLocalDateTime.getPreviousWeekRange()
        Log.d("hrishiiii", "Start of previous week: $startDate")
        Log.d("hrishiiii", "End of previous week: $endDate")
        return transactionsDao.getPreviousWeekTransactionAmounts(userId, startDate, endDate)
            .map { amounts ->
                try {
                    val total = amounts
                        .mapNotNull { it.toBigDecimalOrNull() }
                        .reduceOrNull(BigDecimal::add) ?: BigDecimal.ZERO
                    Result.Success(total)
                } catch (e: Exception) {
                    Result.Error(DataError.Local.UNKNOWN_DATABASE_ERROR)
                }
            }
            .catch { e ->
                if (e is CancellationException) throw e
                emit(Result.Error(DataError.Local.UNKNOWN_DATABASE_ERROR))
            }
            .flowOn(Dispatchers.IO)
    }

    override suspend fun getTransactionsForDateRange(
        userId: Long,
        startDate: LocalDateTime,
        endDate: LocalDateTime
    ): List<Transaction> = try {
        withContext(Dispatchers.IO) {
            transactionsDao.getTransactionsForDateRange(userId, startDate, endDate)
                .map { it.toTransaction() }
        }
    } catch (e: Exception) {
        if (e is CancellationException) throw e
        emptyList()
    }
}