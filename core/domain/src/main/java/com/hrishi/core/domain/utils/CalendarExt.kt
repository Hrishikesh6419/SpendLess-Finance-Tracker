package com.hrishi.core.domain.utils

import java.time.DayOfWeek
import java.time.Instant
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAdjusters

fun LocalDateTime.atEndOfDay(): LocalDateTime {
    return this.toLocalDate().atTime(23, 59, 59, 999_999_999)
}

fun LocalDateTime.getPreviousWeekRange(): Pair<LocalDateTime, LocalDateTime> {
    val startOfPreviousWeek = this
        .with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY))
        .minusWeeks(1)
        .with(LocalTime.MIN)

    val endOfPreviousWeek = startOfPreviousWeek
        .plusDays(6)
        .with(LocalTime.MAX)

    return startOfPreviousWeek to endOfPreviousWeek
}

fun LocalDateTime.toEpochMillis(zoneId: ZoneId = ZoneId.of("America/New_York")): Long {
    return this.atZone(zoneId).toInstant().toEpochMilli()
}

fun Long.toLocalDateTime(zoneId: ZoneId = ZoneId.of("America/New_York")): LocalDateTime {
    return Instant.ofEpochMilli(this).atZone(zoneId).toLocalDateTime()
}

fun LocalDateTime.getFormattedDayOfWeek(): String {
    return this.dayOfWeek.name.lowercase().replaceFirstChar { it.uppercase() }
}

fun LocalDateTime.getFormattedDayOfMonth(): String {
    return this.dayOfMonth.toString()
}

fun LocalDateTime.getMonthAndDay(): String {
    return this.format(DateTimeFormatter.ofPattern("MMM d"))
}

fun LocalDateTime.toShortDateString(): String {
    val formatter = DateTimeFormatter.ofPattern("MMM d, yyyy")
    return this.format(formatter)
}

fun LocalDateTime.toISODateString(): String {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    return this.format(formatter)
}