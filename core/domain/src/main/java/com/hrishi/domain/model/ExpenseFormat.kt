package com.hrishi.domain.model

import java.util.Locale
import kotlin.math.abs

enum class ExpenseFormat : PreferenceOption {
    MINUS_PREFIX,
    BRACKETS;

    override fun displayText(number: Double, currency: Currency?, keepDecimal: Boolean): String {
        val absoluteValue = abs(number)

        // Remove decimals if keepDecimal is false
        val formattedNumber = if (keepDecimal) {
            String.format(Locale.US, "%.2f", absoluteValue) // Always show 2 decimal places
        } else {
            if (absoluteValue % 1.0 == 0.0) {
                absoluteValue.toInt()
                    .toString() // Convert to int and remove decimals if whole number
            } else {
                absoluteValue.toString() // Keep as is (prevents trailing zeroes issue)
            }
        }

        // Append currency symbol if available
        val formattedWithCurrency = currency?.let {
            "${it.symbol}$formattedNumber"
        } ?: formattedNumber

        // Apply expense format
        return when (this) {
            MINUS_PREFIX -> if (number < 0) "-$formattedWithCurrency" else formattedWithCurrency
            BRACKETS -> if (number < 0) "($formattedWithCurrency)" else formattedWithCurrency
        }
    }

    companion object {
        fun fromSaveValue(value: String): ExpenseFormat? {
            return when (value) {
                "-" -> MINUS_PREFIX
                "()" -> BRACKETS
                else -> null
            }
        }
    }
}