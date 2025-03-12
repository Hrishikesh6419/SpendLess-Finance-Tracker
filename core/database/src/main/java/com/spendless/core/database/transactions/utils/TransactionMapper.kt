package com.spendless.core.database.transactions.utils

import com.hrishi.core.domain.security.EncryptionService
import com.hrishi.core.domain.transactions.model.Transaction
import com.spendless.core.database.transactions.entity.TransactionEntity

fun Transaction.toTransactionEntity(encryptionService: EncryptionService): TransactionEntity {
    val (encryptedTransactionName, iv) = encryptionService.encrypt(this.transactionName)
    return TransactionEntity(
        transactionId = this.transactionId ?: 0L,
        userId = this.userId,
        transactionType = this.transactionType,
        encryptedTransactionName = "$encryptedTransactionName:$iv",
        amount = this.amount,
        note = this.note,
        transactionCategory = this.transactionCategory,
        transactionDate = this.transactionDate,
        recurringStartDate = this.recurringStartDate,
        recurringTransactionId = this.recurringTransactionId,
        recurringType = this.recurringType,
        nextRecurringDate = this.nextRecurringDate,
    )
}

fun TransactionEntity.toTransaction(encryptionService: EncryptionService): Transaction {
    val (encryptedTransactionName, transactionIv) = encryptedTransactionName.split(":").let {
        if (it.size != 2) "" to "" else it[0] to it[1]
    }

    return Transaction(
        transactionId = this.transactionId,
        userId = this.userId,
        transactionType = this.transactionType,
        transactionName = encryptionService.decrypt(encryptedTransactionName, transactionIv),
        amount = this.amount,
        note = this.note,
        transactionCategory = this.transactionCategory,
        transactionDate = this.transactionDate,
        recurringStartDate = this.recurringStartDate,
        recurringTransactionId = this.recurringTransactionId,
        recurringType = this.recurringType,
        nextRecurringDate = this.nextRecurringDate,
    )
}
