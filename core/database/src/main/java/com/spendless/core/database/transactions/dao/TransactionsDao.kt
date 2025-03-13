package com.spendless.core.database.transactions.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.spendless.core.database.transactions.entity.TransactionEntity
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

@Dao
interface TransactionsDao {

    @Upsert
    suspend fun upsertTransaction(transaction: TransactionEntity): Long

    @Query(
        """
    SELECT * FROM transactions 
    WHERE userId = :userId 
    ORDER BY transactionDate DESC 
    LIMIT CASE WHEN :limit IS NULL THEN -1 ELSE :limit END
    """
    )
    fun getTransactionsForUser(userId: Long, limit: Int? = null): Flow<List<TransactionEntity>>

    @Query(
        """
        SELECT * FROM transactions 
        WHERE nextRecurringDate <= :currentDate
        """
    )
    suspend fun getDueRecurringTransactions(currentDate: LocalDateTime): List<TransactionEntity>

    @Query("SELECT amount FROM transactions WHERE userId = :userId")
    fun getAllTransactionAmounts(userId: Long): Flow<List<String>>

    @Query(
        """
    SELECT transactionCategory 
    FROM transactions 
    WHERE transactionType = 'EXPENSE' AND userId = :userId
    """
    )
    fun getExpenseCategories(userId: Long): Flow<List<String>>

    @Query(
        """
    SELECT * FROM transactions
    WHERE transactionType = 'EXPENSE' AND userId = :userId
    """
    )
    fun getAllExpenses(userId: Long): Flow<List<TransactionEntity>>

    @Query(
        """
    SELECT amount FROM transactions
    WHERE transactionType = 'EXPENSE' 
    AND userId = :userId
    AND transactionDate BETWEEN :startDate AND :endDate
    """
    )
    fun getPreviousWeekTransactionAmounts(
        userId: Long,
        startDate: LocalDateTime,
        endDate: LocalDateTime
    ): Flow<List<String>>

    @Query(
        """
        SELECT * FROM transactions 
        WHERE userId = :userId 
        AND transactionDate BETWEEN :startDate AND :endDate
        ORDER BY transactionDate DESC
        """
    )
    suspend fun getTransactionsForDateRange(
        userId: Long,
        startDate: LocalDateTime,
        endDate: LocalDateTime
    ): List<TransactionEntity>
}