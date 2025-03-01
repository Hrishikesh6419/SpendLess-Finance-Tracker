package com.hrishi.core.domain.model

enum class SessionDuration : SettingsPreferenceOption {
    ONE_MIN,
    FIVE_MIN,
    FIFTEEN_MIN,
    THIRTY_MIN,
    ONE_HOUR;

    override fun displayText(): String {
        return when (this) {
            ONE_MIN -> "1 min"
            FIVE_MIN -> "5 min"
            FIFTEEN_MIN -> "15 min"
            THIRTY_MIN -> "30 min"
            ONE_HOUR -> "1 hour"
        }
    }

    override fun getValueInLong(): Long {
        return when (this) {
            ONE_MIN -> 60
            FIVE_MIN -> 300
            FIFTEEN_MIN -> 900
            THIRTY_MIN -> 1800
            ONE_HOUR -> 3600
        }
    }
}