package com.spendless.dashboard.presentation.create_screen

import com.hrishi.core.presentation.designsystem.model.ExpenseCategoryTypeUI
import com.hrishi.core.presentation.designsystem.model.RecurringTypeUI
import com.hrishi.core.presentation.designsystem.model.TransactionTypeUI
import java.math.BigDecimal

data class CreateTransactionViewState(
    val transactionType: TransactionTypeUI,
    val transactionName: String,
    val transactionNameHint: String,
    val amount: BigDecimal,
    val noteHint: String,
    val note: String,
    val categoryType: ExpenseCategoryTypeUI,
    val recurringType: RecurringTypeUI,
    val isCreateButtonEnabled: Boolean
)