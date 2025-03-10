package com.hrishi.core.domain.transactions.usecases

import com.hrishi.core.domain.model.RecurringType
import com.hrishi.core.domain.model.TransactionCategory
import com.hrishi.core.domain.transactions.model.Transaction
import com.hrishi.core.domain.transactions.model.TransactionGroupItem
import com.hrishi.core.domain.transactions.repository.TransactionRepository
import com.hrishi.core.domain.utils.CalendarUtils
import com.hrishi.core.domain.utils.DataError
import com.hrishi.core.domain.utils.Result
import kotlinx.coroutines.flow.Flow
import java.math.BigDecimal
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.YearMonth
import java.util.Locale

data class TransactionUseCases(
    val insertTransactionUseCase: InsertTransactionUseCase,
    val getTransactionsForUserUseCase: GetTransactionsForUserUseCase,
    val getRecurringTransactionSeriesUseCase: GetRecurringTransactionSeriesUseCase,
    val getDueRecurringTransactionsUseCase: GetDueRecurringTransactionsUseCase,
    val getAccountBalanceUseCase: GetAccountBalanceUseCase,
    val getMostPopularExpenseCategoryUseCase: GetMostPopularExpenseCategoryUseCase,
    val getLargestTransactionUseCase: GetLargestTransactionUseCase,
    val getPreviousWeekTotalUseCase: GetPreviousWeekTotalUseCase,
    val getTransactionsGroupedByDateUseCase: GetTransactionsGroupedByDateUseCase,
    val getNextRecurringDateUseCase: GetNextRecurringDateUseCase
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
    operator fun invoke(
        userId: Long,
        limit: Int? = null
    ): Flow<Result<List<Transaction>, DataError>> {
        return transactionRepository.getTransactionsForUser(userId, limit)
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

class GetPreviousWeekTotalUseCase(
    private val transactionRepository: TransactionRepository
) {
    operator fun invoke(userId: Long): Flow<Result<BigDecimal, DataError>> {
        return transactionRepository.getPreviousWeekTotal(userId)
    }
}

class GetTransactionsGroupedByDateUseCase {
    operator fun invoke(transactions: List<Transaction>): List<TransactionGroupItem> {
        val today = CalendarUtils.currentEstDate
        val yesterday = today.minusDays(1)
        val dateFormatter = DateTimeFormatter.ofPattern("MMMM d")

        return transactions
            .sortedByDescending { it.transactionDate }
            .groupBy { transaction ->
                when (transaction.transactionDate.toLocalDate()) {
                    today -> "TODAY"
                    yesterday -> "YESTERDAY"
                    else -> transaction.transactionDate.format(dateFormatter).uppercase(Locale.US)
                }
            }
            .map { (dateLabel, transactions) ->
                TransactionGroupItem(
                    dateLabel = dateLabel,
                    transactions = transactions
                )
            }
    }
}

class GetNextRecurringDateUseCase {
    operator fun invoke(
        startDate: LocalDateTime = CalendarUtils.currentEstTime,
        lastTransactionDate: LocalDateTime? = null,
        recurringType: RecurringType,
    ): LocalDateTime? {
        return when (recurringType) {
            RecurringType.ONE_TIME -> null

            RecurringType.DAILY -> {
                lastTransactionDate?.plusDays(1) ?: startDate.plusDays(1)
            }

            RecurringType.WEEKLY -> {
                lastTransactionDate?.plusWeeks(1) ?: startDate.plusWeeks(1)
            }

            RecurringType.MONTHLY -> {
                if (lastTransactionDate != null) {
                    val nextTransactionDate = lastTransactionDate.plusMonths(1)

                    // Check if the start date is the last day of the month.
                    // If the start date was on the 31st, adding a month might set the date to the 30th or earlier
                    // in shorter months. To maintain the "last day of the month" pattern, adjust the
                    // nextTransactionDate to the last day of the next month if needed.
                    val isLastDayOfMonth =
                        startDate.dayOfMonth == YearMonth.from(startDate).lengthOfMonth()

                    return if (isLastDayOfMonth) {
                        val nextMonth = YearMonth.from(nextTransactionDate)
                        nextTransactionDate.withDayOfMonth(nextMonth.lengthOfMonth())
                    } else {
                        nextTransactionDate
                    }
                } else {
                    startDate.plusMonths(1)
                }
            }

            RecurringType.YEARLY -> {
                lastTransactionDate?.plusYears(1) ?: startDate.plusYears(1)
            }
        }
    }
}
