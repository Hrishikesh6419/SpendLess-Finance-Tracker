package com.hrishi.core.domain.utils

import jdk.internal.net.http.common.Log
import java.time.DayOfWeek
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAdjusters

object CalendarUtils {
    private val zoneId: ZoneId = ZoneId.of("America/New_York")

    val currentEstTime: LocalDateTime
        get() = LocalDateTime.now(zoneId)

    fun toEpochMillis(localDateTime: LocalDateTime): Long {
        return localDateTime.atZone(zoneId).toInstant().toEpochMilli()
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