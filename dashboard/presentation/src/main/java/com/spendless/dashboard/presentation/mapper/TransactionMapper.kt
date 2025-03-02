package com.spendless.dashboard.presentation.mapper

import com.hrishi.core.domain.transactions.model.Transaction
import com.hrishi.core.domain.transactions.model.TransactionGroupItem
import com.spendless.dashboard.presentation.dashboard.TransactionGroupUIItem
import com.spendless.dashboard.presentation.dashboard.TransactionUIItem

fun Transaction.toTransactionUiItem(): TransactionUIItem {
    return TransactionUIItem(
        transactionCategory = transactionCategory.toTransactionCategoryUI(),
        title = transactionName,
        note = note,
        date = transactionDate,
        amount = amount
    )
}

fun TransactionGroupItem.toUIItem(): TransactionGroupUIItem {
    return TransactionGroupUIItem(
        dateLabel = this.dateLabel,
        transactions = this.transactions.map { it.toUIItem() }
    )
}

fun Transaction.toUIItem(): TransactionUIItem {
    return TransactionUIItem(
        transactionId = this.transactionId ?: 0L,
        transactionCategory = this.transactionCategory.toTransactionCategoryUI(),
        title = this.transactionName,
        note = this.note,
        amount = this.amount,
        date = this.transactionDate
    )
}