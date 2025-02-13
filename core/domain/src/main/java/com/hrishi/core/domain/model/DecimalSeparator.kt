package com.hrishi.core.domain.model

import java.util.Locale

enum class DecimalSeparator : PreferenceOption {
    DOT,
    COMMA;

    override fun displayText(number: Double, currency: Currency?, keepDecimal: Boolean): String {
        return when (this) {
            DOT -> String.format(Locale.US, "%.2f", number)
            COMMA -> String.format(Locale.US, "%.2f", number).replace('.', ',')
        }
    }

    companion object {
        fun fromSaveValue(value: String): DecimalSeparator? {
            return when (value) {
                "." -> DOT
                "," -> COMMA
                else -> null
            }
        }
    }
}