package com.hrishi.domain.usecase

import com.hrishi.domain.formatting.NumberFormatter
import com.hrishi.domain.model.Currency
import com.hrishi.domain.model.DecimalSeparator
import com.hrishi.domain.model.ExpenseFormat
import com.hrishi.domain.model.ThousandsSeparator

data class OnboardingPreferenceUseCases(
    val validateSelectedPreferences: ValidateSelectedPreferences,
    val formatExampleUseCase: FormatExampleUseCase
)

class ValidateSelectedPreferences {
    operator fun invoke(
        decimalSeparator: DecimalSeparator,
        thousandsSeparator: ThousandsSeparator
    ): Boolean {
        return when {
            (decimalSeparator == DecimalSeparator.DOT && thousandsSeparator == ThousandsSeparator.DOT) ||
                    (decimalSeparator == DecimalSeparator.COMMA && thousandsSeparator == ThousandsSeparator.COMMA) -> false

            else -> true
        }
    }
}

class FormatExampleUseCase(private val numberFormatter: NumberFormatter) {
    operator fun invoke(
        expenseFormat: ExpenseFormat,
        decimalSeparator: DecimalSeparator,
        thousandsSeparator: ThousandsSeparator,
        currency: Currency
    ): String {
        return numberFormatter.formatAmount(
            amount = -10382.45,
            expenseFormat = expenseFormat,
            decimalSeparator = decimalSeparator,
            thousandsSeparator = thousandsSeparator,
            currency = currency
        )
    }
}