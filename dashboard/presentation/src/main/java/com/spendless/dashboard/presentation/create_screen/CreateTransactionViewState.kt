package com.spendless.dashboard.presentation.create_screen

import com.hrishi.core.presentation.designsystem.model.CategoryTypeUI
import com.hrishi.core.presentation.designsystem.model.RecurringTypeUI
import com.hrishi.core.presentation.designsystem.model.TransactionTypeUI
import java.math.BigDecimal

data class CreateTransactionViewState(
    val transactionType: TransactionTypeUI,
    val transactionName: String,
    val transactionNameHint: String,
    val amount: BigDecimal,
    val note: String,
    val categoryType: CategoryTypeUI,
    val recurringType: RecurringTypeUI,
    val isCreateButtonEnabled: Boolean
)