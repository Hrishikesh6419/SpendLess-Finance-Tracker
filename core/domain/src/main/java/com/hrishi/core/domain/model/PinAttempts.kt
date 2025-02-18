package com.hrishi.core.domain.model

enum class PinAttempts {
    THREE,
    FIVE,
    SEVEN,
    TEN;

    fun getValue(): Long {
        return when (this) {
            THREE -> 3
            FIVE -> 5
            SEVEN -> 7
            TEN -> 10
        }
    }
}