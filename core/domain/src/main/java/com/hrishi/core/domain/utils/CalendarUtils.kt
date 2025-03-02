package com.hrishi.core.domain.utils

import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

object CalendarUtils {
    private val zoneId: ZoneId = ZoneId.of("America/New_York")

    val currentEstTime: LocalDateTime
        get() = LocalDateTime.now(zoneId)

    fun toEpochMillis(localDateTime: LocalDateTime): Long {
        return localDateTime.atZone(zoneId).toInstant().toEpochMilli()
    }
}

fun LocalDateTime.toShortDateString(): String {
    val formatter = DateTimeFormatter.ofPattern("MMM d, yyyy")
    return this.format(formatter)
}