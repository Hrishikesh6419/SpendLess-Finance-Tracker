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

    val currentEstTime: LocalDateTime
        get() = LocalDateTime.now(zoneId)

    val currentEstDate: LocalDate
        get() = LocalDate.now(zoneId)

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
            .with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY))
            .minusWeeks(1)
            .with(LocalTime.MIN)

        val endOfPreviousWeek = now
            .with(TemporalAdjusters.previousOrSame(DayOfWeek.SATURDAY))
            .with(LocalTime.MAX)
        return startOfPreviousWeek to endOfPreviousWeek
    }
}

fun LocalDateTime.toShortDateString(): String {
    val formatter = DateTimeFormatter.ofPattern("MMM d, yyyy")
    return this.format(formatter)
}