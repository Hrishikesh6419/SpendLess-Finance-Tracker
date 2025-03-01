package com.spendless.settings.presentation.preference

import com.hrishi.core.domain.formatting.NumberFormatter
import com.hrishi.core.domain.model.Currency
import com.hrishi.core.domain.model.DecimalSeparator
import com.hrishi.core.domain.model.ExpenseFormat
import com.hrishi.core.domain.model.ThousandsSeparator
import java.math.BigDecimal

data class SettingsPreferencesViewState(
    val userId: Long = 0L,
    val expenseFormat: ExpenseFormat = ExpenseFormat.MINUS_PREFIX,
    val currency: Currency = Currency.USD,
    val decimalSeparator: DecimalSeparator = DecimalSeparator.DOT,
    val thousandsSeparator: ThousandsSeparator = ThousandsSeparator.COMMA,
    val amount: BigDecimal = BigDecimal(-10382.45),
    val enableSaveButton: Boolean = true,
    val exampleFormat: String = NumberFormatter.formatAmount(
        amount = amount,
        expenseFormat = expenseFormat,
        decimalSeparator = decimalSeparator,
        thousandsSeparator = thousandsSeparator,
        currency = currency
    )
)