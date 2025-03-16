package com.hrishi.core.domain.model

import com.hrishi.core.domain.utils.getFormattedDayOfMonth
import com.hrishi.core.domain.utils.getFormattedDayOfWeek
import com.hrishi.core.domain.utils.getMonthAndDay
import java.time.LocalDateTime

enum class RecurringType {
    ONE_TIME,
    DAILY,
    WEEKLY,
    MONTHLY,
    YEARLY;

    fun exportTitle(recurringStartDate: LocalDateTime): String {
        return when (this) {
            ONE_TIME -> "One Time"
            DAILY -> "Daily"
            WEEKLY -> "Weekly on ${recurringStartDate.getFormattedDayOfWeek()}"
            MONTHLY -> "Monthly on the ${recurringStartDate.getFormattedDayOfMonth()}th"
            YEARLY -> "Yearly on ${recurringStartDate.getMonthAndDay()}th"
        }
    }
}