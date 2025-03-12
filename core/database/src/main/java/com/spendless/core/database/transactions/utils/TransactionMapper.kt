package com.spendless.core.database.transactions.utils

import com.hrishi.core.domain.security.EncryptionService
import com.hrishi.core.domain.transactions.model.Transaction
import com.spendless.core.database.transactions.entity.TransactionEntity

fun Transaction.toTransactionEntity(encryptionService: EncryptionService): TransactionEntity {
    return TransactionEntity(
        transactionId = this.transactionId ?: 0L,
        userId = this.userId,
        transactionType = this.transactionType,
        transactionNameEncrypted = encryptionService.encrypt(this.transactionName),
        amount = this.amount,
        noteEncrypted = encryptionService.encrypt(this.note ?: ""),
        transactionCategory = this.transactionCategory,
        transactionDate = this.transactionDate,
        recurringStartDate = this.recurringStartDate,
        recurringTransactionId = this.recurringTransactionId,
        recurringType = this.recurringType,
        nextRecurringDate = this.nextRecurringDate,
    )
}

fun TransactionEntity.toTransaction(encryptionService: EncryptionService): Transaction {
    return Transaction(
        transactionId = this.transactionId,
        userId = this.userId,
        transactionType = this.transactionType,
        transactionName = encryptionService.decrypt(this.transactionNameEncrypted),
        amount = this.amount,
        note = this.noteEncrypted?.let {
            encryptionService.decrypt(it)
        } ?: "",
        transactionCategory = this.transactionCategory,
        transactionDate = this.transactionDate,
        recurringStartDate = this.recurringStartDate,
        recurringTransactionId = this.recurringTransactionId,
        recurringType = this.recurringType,
        nextRecurringDate = this.nextRecurringDate,
    )
}
