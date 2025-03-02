package com.hrishi.core.domain.transactions.usecases

import com.hrishi.core.domain.model.TransactionCategory
import com.hrishi.core.domain.transactions.model.Transaction
import com.hrishi.core.domain.transactions.repository.TransactionRepository
import com.hrishi.core.domain.utils.DataError
import com.hrishi.core.domain.utils.Result
import kotlinx.coroutines.flow.Flow
import java.math.BigDecimal
import java.time.LocalDateTime

data class TransactionUseCases(
    val insertTransactionUseCase: InsertTransactionUseCase,
    val getTransactionsForUserUseCase: GetTransactionsForUserUseCase,
    val getRecurringTransactionSeriesUseCase: GetRecurringTransactionSeriesUseCase,
    val getDueRecurringTransactionsUseCase: GetDueRecurringTransactionsUseCase,
    val getAccountBalanceUseCase: GetAccountBalanceUseCase,
    val getMostPopularExpenseCategoryUseCase: GetMostPopularExpenseCategoryUseCase,
    val getLargestTransactionUseCase: GetLargestTransactionUseCase,
)

class InsertTransactionUseCase(
    private val transactionRepository: TransactionRepository
) {
    suspend operator fun invoke(transaction: Transaction): Result<Unit, DataError> {
        return transactionRepository.insertTransaction(transaction)
    }
}

class GetTransactionsForUserUseCase(
    private val transactionRepository: TransactionRepository
) {
    operator fun invoke(userId: Long): Flow<Result<List<Transaction>, DataError>> {
        return transactionRepository.getTransactionsForUser(userId)
    }
}

class GetRecurringTransactionSeriesUseCase(
    private val transactionRepository: TransactionRepository
) {
    operator fun invoke(recurringId: Long): Flow<Result<List<Transaction>, DataError>> {
        return transactionRepository.getRecurringTransactionSeries(recurringId)
    }
}

class GetDueRecurringTransactionsUseCase(
    private val transactionRepository: TransactionRepository
) {
    suspend operator fun invoke(currentDate: LocalDateTime): Result<List<Transaction>, DataError> {
        return transactionRepository.getDueRecurringTransactions(currentDate)
    }
}

class GetAccountBalanceUseCase(
    private val transactionRepository: TransactionRepository
) {
    operator fun invoke(userId: Long): Flow<Result<BigDecimal, DataError>> {
        return transactionRepository.getAccountBalance(userId)
    }
}

class GetMostPopularExpenseCategoryUseCase(
    private val transactionRepository: TransactionRepository
) {
    operator fun invoke(userId: Long): Flow<Result<TransactionCategory?, DataError>> {
        return transactionRepository.getMostPopularExpenseCategory(userId)
    }
}

class GetLargestTransactionUseCase(
    private val transactionRepository: TransactionRepository
) {
    operator fun invoke(userId: Long): Flow<Result<Transaction?, DataError>> {
        return transactionRepository.getLargestTransaction(userId)
    }
}