package com.hrishi.presentation.ui.utils

import com.hrishi.core.domain.utils.getFormattedDayOfMonth
import com.hrishi.core.domain.utils.getFormattedDayOfWeek
import com.hrishi.core.domain.utils.getMonthAndDay
import com.hrishi.core.presentation.designsystem.model.RecurringTypeUI
import java.time.LocalDateTime

fun RecurringTypeUI.getFormattedTitle(localDateTime: LocalDateTime): String {
    return when (this) {
        RecurringTypeUI.WEEKLY -> String.format(this.title, localDateTime.getFormattedDayOfWeek())
        RecurringTypeUI.MONTHLY -> String.format(
            this.title,
            "${localDateTime.getFormattedDayOfMonth()}th"
        )

        RecurringTypeUI.YEARLY -> String.format(
            this.title,
            "${localDateTime.getMonthAndDay()}th"
        )

        else -> this.title
    }
}