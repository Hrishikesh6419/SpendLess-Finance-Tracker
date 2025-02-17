package com.hrishi.core.domain.model

interface PreferenceOption {
    fun displayText(
        number: Double,
        currency: Currency? = null,
        keepDecimal: Boolean = true
    ): String
}

interface SettingsPreferenceOption {
    fun displayText(text: String): String
    fun getValueInLong(): Long
}