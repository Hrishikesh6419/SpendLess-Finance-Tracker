package com.hrishi.auth.presentation.user_preference

import com.hrishi.domain.formatting.NumberFormatter
import com.hrishi.domain.model.Currency
import com.hrishi.domain.model.DecimalSeparator
import com.hrishi.domain.model.ExpenseFormat
import com.hrishi.domain.model.ThousandsSeparator

data class OnboardingPreferencesViewState(
    val expenseFormat: ExpenseFormat = ExpenseFormat.MINUS_PREFIX,
    val currency: Currency = Currency.USD,
    val decimalSeparator: DecimalSeparator = DecimalSeparator.DOT,
    val thousandsSeparator: ThousandsSeparator = ThousandsSeparator.COMMA,
    val enableStartTracking: Boolean = true,
    val exampleFormat: String = NumberFormatter.formatAmount(
        amount = -10382.45,
        expenseFormat = expenseFormat,
        decimalSeparator = decimalSeparator,
        thousandsSeparator = thousandsSeparator,
        currency = currency
    )
)
