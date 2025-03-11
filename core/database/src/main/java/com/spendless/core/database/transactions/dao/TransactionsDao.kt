package com.spendless.core.database.transactions.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.hrishi.core.domain.model.TransactionCategory
import com.spendless.core.database.transactions.entity.TransactionEntity
import kotlinx.coroutines.flow.Flow
import java.math.BigDecimal
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
        AND recurringType != 'ONE_TIME'
        """
    )
    suspend fun getDueRecurringTransactions(currentDate: LocalDateTime): List<TransactionEntity>

    // COALESCE(..., 0): Ensures that if there are no transactions, it returns 0 instead of null.
    @Query("SELECT COALESCE(SUM(amount), '0') FROM transactions WHERE userId = :userId")
    fun getAccountBalance(userId: Long): Flow<String>

    @Query(
        """
    SELECT transactionCategory
    FROM transactions
    WHERE transactionType = 'EXPENSE' AND userId = :userId
    GROUP BY transactionCategory
    ORDER BY COUNT(transactionCategory) DESC
    LIMIT 1
    """
    )
    fun getMostPopularExpenseCategory(userId: Long): Flow<TransactionCategory?>

    @Query(
        """
    SELECT * FROM transactions
    WHERE transactionType = 'EXPENSE' AND userId = :userId
    ORDER BY amount DESC
    LIMIT 1
    """
    )
    fun getLargestTransaction(userId: Long): Flow<TransactionEntity?>

    @Query(
        """
    SELECT COALESCE(SUM(amount), '0') FROM transactions
    WHERE transactionType = 'EXPENSE' 
    AND userId = :userId
    AND transactionDate BETWEEN :startDate AND :endDate
    """
    )
    fun getPreviousWeekTotal(
        userId: Long,
        startDate: LocalDateTime,
        endDate: LocalDateTime
    ): Flow<BigDecimal>

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