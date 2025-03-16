package com.hrishi.core.domain.export.model

import com.hrishi.core.domain.utils.CalendarUtils
import java.time.LocalDateTime

enum class ExportType(val displayName: String) {
    ALL_DATA("All data"),
    LAST_THREE_MONTH("Last three months"),
    LAST_MONTH("Last month"),
    CURRENT_MONTH("Current month");

    fun getDateRange(): Pair<LocalDateTime, LocalDateTime>? {
        val now = CalendarUtils.currentEstTime
        return when (this) {
            ALL_DATA -> null
            LAST_THREE_MONTH -> now.minusMonths(3).withDayOfMonth(1) to now
            LAST_MONTH -> now.minusMonths(1).withDayOfMonth(1) to now.withDayOfMonth(1).minusDays(1)
            CURRENT_MONTH -> now.withDayOfMonth(1) to now
        }
    }
}