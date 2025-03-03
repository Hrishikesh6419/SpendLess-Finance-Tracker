package com.hrishi.core.domain.formatting

import com.hrishi.core.domain.model.Currency
import com.hrishi.core.domain.model.DecimalSeparator
import com.hrishi.core.domain.model.ExpenseFormat
import com.hrishi.core.domain.model.ThousandsSeparator
import com.hrishi.core.domain.preference.model.UserPreferences
import java.math.BigDecimal
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.NumberFormat
import java.util.Locale

object NumberFormatter {
    fun formatAmount(
        amount: BigDecimal,
        expenseFormat: ExpenseFormat,
        decimalSeparator: DecimalSeparator,
        thousandsSeparator: ThousandsSeparator,
        currency: Currency
    ): String {
        val isNegativeNumber = amount < BigDecimal.ZERO
        val absoluteAmount = amount.abs()

        val numberFormat = NumberFormat.getNumberInstance(Locale.US) as DecimalFormat
        val symbols = DecimalFormatSymbols(Locale.US)

        symbols.decimalSeparator = decimalSeparator.toValue()
        symbols.groupingSeparator = thousandsSeparator.toValue()

        numberFormat.decimalFormatSymbols = symbols
        numberFormat.minimumFractionDigits = 2
        numberFormat.maximumFractionDigits = 2
        numberFormat.isGroupingUsed = true

        val formattedNumber = numberFormat.format(absoluteAmount)
        val formattedNumberWithCurrency = "${currency.symbol}$formattedNumber"

        // Formatted for expense format, i.e, minus or brackets
        return if (isNegativeNumber) {
            expenseFormat.toValue(formattedNumberWithCurrency)
        } else formattedNumberWithCurrency
    }

    fun formatAmount(amount: BigDecimal, preferences: UserPreferences?): String {
        return preferences?.let {
            formatAmount(
                amount = amount,
                expenseFormat = it.expenseFormat,
                decimalSeparator = it.decimalSeparator,
                thousandsSeparator = it.thousandsSeparator,
                currency = it.currency
            )
        } ?: ""
    }
}