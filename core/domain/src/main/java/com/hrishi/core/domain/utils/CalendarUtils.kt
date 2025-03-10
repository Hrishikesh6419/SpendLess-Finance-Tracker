package com.hrishi.core.domain.utils

import java.time.DayOfWeek
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAdjusters

object CalendarUtils {
    private val zoneId: ZoneId = ZoneId.of("America/New_York")

    // TODO: Revisit these dateTime
    val currentEstTime: LocalDateTime
        get() = LocalDateTime.now(zoneId)

    val currentEstDate: LocalDate
        get() = LocalDate.now(zoneId)

    val currentEstTimeAtStartOfDay: LocalDateTime
        get() = currentEstDate.atStartOfDay()

    val currentEstTimeAtEndOfDay: LocalDateTime
        get() = currentEstDate.atTime(23, 59, 59, 999999999)

    fun toEpochMillis(localDateTime: LocalDateTime): Long {
        return localDateTime.atZone(zoneId).toInstant().toEpochMilli()
    }

    fun epochToLocalDateTime(epochMillis: Long): LocalDateTime {
        return Instant.ofEpochMilli(epochMillis)
            .atZone(zoneId)
            .toLocalDateTime()
    }

    fun getPreviousWeekRange(): Pair<LocalDateTime, LocalDateTime> {
        val now = currentEstTime

        val startOfPreviousWeek = now
            .with(TemporalAdjusters.previous(DayOfWeek.SUNDAY))
            .minusWeeks(1)
            .with(LocalTime.MIN)

        val endOfPreviousWeek = startOfPreviousWeek
            .plusDays(6)
            .with(LocalTime.MAX)
        return startOfPreviousWeek to endOfPreviousWeek
    }

    fun getCurrentDayOfWeek(): String {
        return currentEstDate.dayOfWeek.name.lowercase().replaceFirstChar { it.uppercase() }
    }

    fun getCurrentDayOfMonth(): String {
        return currentEstDate.dayOfMonth.toString()
    }

    fun getCurrentMonthAndDay(): String {
        val formatter = DateTimeFormatter.ofPattern("MMM d")
        return currentEstDate.format(formatter)
    }
}

fun LocalDateTime.toShortDateString(): String {
    val formatter = DateTimeFormatter.ofPattern("MMM d, yyyy")
    return this.format(formatter)
}

fun LocalDateTime.toISODateString(): String {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    return this.format(formatter)
}