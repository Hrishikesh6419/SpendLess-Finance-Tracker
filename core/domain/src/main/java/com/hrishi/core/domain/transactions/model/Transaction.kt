package com.hrishi.core.domain.transactions.model

import com.hrishi.core.domain.model.RecurringType
import com.hrishi.core.domain.model.TransactionCategory
import com.hrishi.core.domain.model.TransactionType
import java.math.BigDecimal
import java.time.LocalDateTime

data class Transaction(
    val transactionId: Long?,
    val userId: Long,
    val transactionType: TransactionType,
    val transactionName: String,
    val amount: BigDecimal,
    val note: String?,
    val transactionCategory: TransactionCategory,
    val transactionDate: LocalDateTime,
    val recurringStartDate: LocalDateTime,
    val recurringTransactionId: Long?,
    val recurringType: RecurringType,
    val nextRecurringDate: LocalDateTime?
)