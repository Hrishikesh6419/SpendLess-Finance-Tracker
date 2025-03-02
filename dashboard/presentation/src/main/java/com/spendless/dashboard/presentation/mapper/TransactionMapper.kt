package com.spendless.dashboard.presentation.mapper

import com.hrishi.core.domain.transactions.model.Transaction
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