package com.hrishi.core.domain.model

import java.util.Locale
import java.math.BigDecimal
import java.math.RoundingMode

enum class ExpenseFormat : PreferenceOption {
    MINUS_PREFIX,
    BRACKETS;

    override fun displayText(number: BigDecimal, currency: Currency?, keepDecimal: Boolean): String {
        val absoluteValue = number.abs()

        // Remove decimals if keepDecimal is false
        val formattedNumber = if (keepDecimal) {
            String.format(Locale.US, "%.2f", absoluteValue) // Always show 2 decimal places
        } else {
            if (absoluteValue.remainder(BigDecimal.ONE).compareTo(BigDecimal.ZERO) == 0) {
                absoluteValue.setScale(0, RoundingMode.DOWN).toString() // Convert to int and remove decimals if whole number
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
            MINUS_PREFIX -> if (number < BigDecimal.ZERO) "-$formattedWithCurrency" else formattedWithCurrency
            BRACKETS -> if (number < BigDecimal.ZERO) "($formattedWithCurrency)" else formattedWithCurrency
        }
    }

    fun toValue(number: String): String {
        return when (this) {
            MINUS_PREFIX -> "-$number"
            BRACKETS -> "($number)"
        }
    }
}