package com.hrishi.domain.formatting

import com.hrishi.domain.model.Currency
import com.hrishi.domain.model.DecimalSeparator
import com.hrishi.domain.model.ExpenseFormat
import com.hrishi.domain.model.ThousandsSeparator
import java.util.Locale

object NumberFormatter {
    fun formatAmount(
        amount: Double,
        expenseFormat: ExpenseFormat,
        decimalSeparator: DecimalSeparator,
        thousandsSeparator: ThousandsSeparator,
        currency: Currency
    ): String {
        val isNegative = amount < 0
        val absoluteAmount = kotlin.math.abs(amount)

        // Format number using US Locale (default is dot as decimal separator, comma for thousands)
        var formattedNumber = String.format(Locale.US, "%,.2f", absoluteAmount)

        // Step 1: Convert thousands separator to a temporary placeholder
        formattedNumber = when (thousandsSeparator) {
            ThousandsSeparator.DOT -> formattedNumber.replace(',', '¤')  // Use ¤ (temp char)
            ThousandsSeparator.COMMA -> formattedNumber  // No change (US default)
            ThousandsSeparator.SPACE -> formattedNumber.replace(',', ' ') // 1,000 → 1 000
        }

        // Step 2: Convert decimal separator (Ensure it doesn't overwrite thousands)
        formattedNumber = when (decimalSeparator) {
            DecimalSeparator.DOT -> formattedNumber.replace('.', '.')  // No change needed
            DecimalSeparator.COMMA -> formattedNumber.replace(
                '.',
                ','
            ) // 1.000 → 1,000 OR 1¤000 → 1¤000
        }

        // Step 3: Convert the temporary placeholder back to the correct thousands separator
        formattedNumber = when (thousandsSeparator) {
            ThousandsSeparator.DOT -> formattedNumber.replace(
                '¤',
                '.'
            )  // Restore dot for thousands
            ThousandsSeparator.COMMA -> formattedNumber  // No change
            ThousandsSeparator.SPACE -> formattedNumber  // Already handled
        }

        // Step 4: Apply expense format with currency
        return when {
            isNegative && expenseFormat == ExpenseFormat.MINUS_PREFIX -> "-${currency.symbol}$formattedNumber"
            isNegative && expenseFormat == ExpenseFormat.BRACKETS -> "(${currency.symbol}$formattedNumber)"
            else -> "${currency.symbol}$formattedNumber"
        }
    }
}

