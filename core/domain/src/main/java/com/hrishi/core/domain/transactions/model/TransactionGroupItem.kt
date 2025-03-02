package com.hrishi.core.domain.transactions.model

data class TransactionGroupItem(
    val dateLabel: String,
    val transactions: List<Transaction>
)