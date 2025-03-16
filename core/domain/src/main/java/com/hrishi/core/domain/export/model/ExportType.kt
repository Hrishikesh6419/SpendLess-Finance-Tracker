package com.hrishi.core.domain.export.model

import java.time.LocalDateTime

enum class ExportType(val displayName: String) {
    ALL_DATA("All data"),
    LAST_THREE_MONTH("Last three months"),
    LAST_MONTH("Last month"),
    CURRENT_MONTH("Current month");

    fun getDateRange(timeNow: LocalDateTime): Pair<LocalDateTime, LocalDateTime>? {
        return when (this) {
            ALL_DATA -> null
            LAST_THREE_MONTH -> timeNow.minusMonths(3).withDayOfMonth(1) to timeNow
            LAST_MONTH -> timeNow.minusMonths(1).withDayOfMonth(1) to timeNow.withDayOfMonth(1)
                .minusDays(1)

            CURRENT_MONTH -> timeNow.withDayOfMonth(1) to timeNow
        }
    }
}