package com.hrishi.core.domain.model

import java.math.BigDecimal
import java.util.Locale

enum class DecimalSeparator : PreferenceOption {
    DOT,
    COMMA;

    override fun displayText(number: BigDecimal, currency: Currency?, keepDecimal: Boolean): String {
        return when (this) {
            DOT -> String.format(Locale.US, "%.2f", number)
            COMMA -> String.format(Locale.US, "%.2f", number).replace('.', ',')
        }
    }

    fun toValue(): Char {
        return when (this) {
            DOT -> '.'
            COMMA -> ','
        }
    }
}