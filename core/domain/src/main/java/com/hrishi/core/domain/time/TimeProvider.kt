package com.hrishi.core.domain.time

import java.time.LocalDateTime

interface TimeProvider {
    val currentLocalDateTime: LocalDateTime
}