package com.hrishi.domain.model

interface PreferenceOption {
    fun displayText(
        number: Double,
        currency: Currency? = null,
        keepDecimal: Boolean = true
    ): String
}