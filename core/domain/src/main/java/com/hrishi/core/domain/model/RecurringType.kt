package com.hrishi.core.domain.model

import com.hrishi.core.domain.utils.CalendarUtils
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
            WEEKLY -> "Weekly on ${CalendarUtils.getDayOfWeek(recurringStartDate)}"
            MONTHLY -> "Monthly on the ${CalendarUtils.getDayOfMonth(recurringStartDate)}th"
            YEARLY -> "Yearly on ${CalendarUtils.getMonthAndDay(recurringStartDate)}th"
        }
    }
}