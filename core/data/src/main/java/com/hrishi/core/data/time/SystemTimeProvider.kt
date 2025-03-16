package com.hrishi.core.data.time

import com.hrishi.core.domain.time.TimeProvider
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId

class SystemTimeProvider(
    private val zoneId: ZoneId
) : TimeProvider {

    override val currentLocalDateTime: LocalDateTime
        get() = LocalDateTime.now(zoneId)
}