package com.spendless.dashboard.presentation.all_transactions

import com.hrishi.core.domain.preference.model.UserPreferences
import com.spendless.dashboard.presentation.dashboard.TransactionGroupUIItem

data class AllTransactionsViewState(
    val preference: UserPreferences? = null,
    val transactions: List<TransactionGroupUIItem>? = null,
    val showCreateTransactionsSheet: Boolean = false,
    val showExportTransactionsSheet: Boolean = false,
)