package com.spendless.dashboard.presentation.dashboard

import com.hrishi.core.presentation.designsystem.model.ExpenseCategoryTypeUI
import java.math.BigDecimal

data class DashboardViewState(
    val username: String = "",
    val accountBalance: String = "",
    val mostPopularCategory: String = "",
    val largestTransaction: LargestTransaction? = null,
    val previousWeekTotal: BigDecimal = BigDecimal.ZERO,
    val transactions: List<TransactionUIItem>? = null,
    val isSessionExpired: Boolean = false,
    val showCreateTransactionSheet: Boolean = false
)

data class LargestTransaction(
    val name: String,
    val amount: BigDecimal,
    val date: Long
)

data class TransactionUIItem(
    val expenseCategory: ExpenseCategoryTypeUI,
    val title: String,
    val note: String? = null,
    val amount: BigDecimal,
    val isCollapsed: Boolean = true
)