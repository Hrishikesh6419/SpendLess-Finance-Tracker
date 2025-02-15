package com.hrishi.core.domain.formatting

import com.hrishi.core.domain.model.Currency
import com.hrishi.core.domain.model.DecimalSeparator
import com.hrishi.core.domain.model.ExpenseFormat
import com.hrishi.core.domain.model.ThousandsSeparator
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.NumberFormat
import java.util.Locale

object NumberFormatter {
    fun formatAmount(
        amount: Double,
        expenseFormat: ExpenseFormat,
        decimalSeparator: DecimalSeparator,
        thousandsSeparator: ThousandsSeparator,
        currency: Currency
    ): String {
        val isNegativeNumber = amount < 0
        val absoluteAmount = kotlin.math.abs(amount)

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
}