package com.hrishi.core.domain.model

enum class LockoutDuration : SettingsPreferenceOption {
    FIFTEEN_SECONDS,
    THIRTY_SECONDS,
    ONE_MIN,
    FIVE_MIN;

    override fun displayText(): String {
        return when (this) {
            FIFTEEN_SECONDS -> "15s"
            THIRTY_SECONDS -> "30s"
            ONE_MIN -> "1 min"
            FIVE_MIN -> "5 min"
        }
    }

    override fun getValueInLong(): Long {
        return when (this) {
            FIFTEEN_SECONDS -> 15
            THIRTY_SECONDS -> 30
            ONE_MIN -> 60
            FIVE_MIN -> 300
        }
    }
}