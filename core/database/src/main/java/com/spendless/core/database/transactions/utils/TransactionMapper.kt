package com.spendless.core.database.transactions.utils

import com.hrishi.core.domain.transactions.model.Transaction
import com.spendless.core.database.transactions.entity.TransactionEntity

fun Transaction.toTransactionEntity(): TransactionEntity {
    return TransactionEntity(
        transactionId = this.transactionId ?: 0L,
        userId = this.userId,
        transactionType = this.transactionType,
        transactionName = this.transactionName,
        amount = this.amount,
        note = this.note,
        expenseCategory = this.expenseCategory,
        transactionDate = this.transactionDate,
        recurringTransactionId = this.recurringTransactionId,
        recurringType = this.recurringType,
        nextRecurringDate = this.nextRecurringDate,
        endDate = this.endDate
    )
}

fun TransactionEntity.toTransaction(): Transaction {
    return Transaction(
        transactionId = this.transactionId,
        userId = this.userId,
        transactionType = this.transactionType,
        transactionName = this.transactionName,
        amount = this.amount,
        note = this.note,
        expenseCategory = this.expenseCategory,
        transactionDate = this.transactionDate,
        recurringTransactionId = this.recurringTransactionId,
        recurringType = this.recurringType,
        nextRecurringDate = this.nextRecurringDate,
        endDate = this.endDate
    )
}
