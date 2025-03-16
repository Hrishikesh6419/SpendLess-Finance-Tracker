package com.spendless.dashboard.presentation.create_screen

import com.hrishi.core.domain.model.Currency
import com.hrishi.core.presentation.designsystem.components.text_field.DecimalSeparatorUI
import com.hrishi.core.presentation.designsystem.components.text_field.ExpenseFormatUI
import com.hrishi.core.presentation.designsystem.components.text_field.ThousandsSeparatorUI
import com.hrishi.core.presentation.designsystem.model.RecurringTypeUI
import com.hrishi.core.presentation.designsystem.model.TransactionCategoryTypeUI
import com.hrishi.core.presentation.designsystem.model.TransactionTypeUI
import java.math.BigDecimal
import java.time.LocalDateTime

data class CreateTransactionViewState(
    val transactionType: TransactionTypeUI,
    val transactionName: String,
    val transactionNameHint: String,
    val amount: BigDecimal,
    val noteHint: String,
    val note: String,
    val showExpenseCategoryType: Boolean,
    val transactionCategoryType: TransactionCategoryTypeUI,
    val recurringType: RecurringTypeUI,
    val userId: Long? = null,
    val currency: Currency? = null,
    val expenseFormat: ExpenseFormatUI? = null,
    val decimalSeparatorUI: DecimalSeparatorUI? = null,
    val thousandsSeparatorUI: ThousandsSeparatorUI? = null,
    val isCreateButtonEnabled: Boolean,
    val currentTime: LocalDateTime
)