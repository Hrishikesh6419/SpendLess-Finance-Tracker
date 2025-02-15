package com.hrishi.core.domain.model

import java.util.Locale

enum class ThousandsSeparator : PreferenceOption {
    DOT,
    COMMA,
    SPACE;

    override fun displayText(number: Double, currency: Currency?, keepDecimal: Boolean): String {
        val locale = Locale.US

        // Determine decimal format based on `keepDecimal`
        val format = if (keepDecimal) {
            "%,.2f" // Always 2 decimal places
        } else {
            if (number % 1.0 == 0.0) "%,.0f" else "%,.2f" // Remove decimals if whole
        }

        val formattedNumber = String.format(locale, format, number)

        return when (this) {
            DOT -> formattedNumber.replace(",", ".")
            COMMA -> formattedNumber
            SPACE -> formattedNumber.replace(",", " ")
        }
    }

    fun toValue(): Char {
        return when (this) {
            DOT -> '.'
            COMMA -> ','
            SPACE -> ' '
        }
    }
}