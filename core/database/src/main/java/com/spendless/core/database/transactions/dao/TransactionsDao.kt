package com.spendless.core.database.transactions.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.hrishi.core.domain.model.TransactionCategory
import com.spendless.core.database.transactions.entity.TransactionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionsDao {

    @Upsert
    suspend fun upsertTransaction(transaction: TransactionEntity): Long

    @Query("SELECT * FROM transactions WHERE userId = :userId ORDER BY transactionDate DESC")
    fun getTransactionsForUser(userId: Long): Flow<List<TransactionEntity>>

    @Query("SELECT * FROM transactions WHERE recurringTransactionId = :recurringId ORDER BY transactionDate ASC")
    fun getRecurringTransactionSeries(recurringId: Long): Flow<List<TransactionEntity>>

    @Query(
        """
        SELECT * FROM transactions 
        WHERE nextRecurringDate <= :currentDate 
        AND recurringType != 'ONE_TIME'
        AND (endDate IS NULL OR nextRecurringDate <= endDate)
    """
    )
    suspend fun getDueRecurringTransactions(currentDate: Long): List<TransactionEntity>

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
    fun getMostPopularExpenseCategory(userId: Long): Flow<TransactionCategory>
}