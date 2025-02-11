package com.hrishi.domain.model

enum class ExpenseFormat : PreferenceOption {
    MINUS_PREFIX,
    BRACKETS;

    override fun displayText(number: Double): String {
        return when (this) {
            MINUS_PREFIX -> if (number < 0) "-$number" else "$number"
            BRACKETS -> if (number < 0) "($number)" else "$number"
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