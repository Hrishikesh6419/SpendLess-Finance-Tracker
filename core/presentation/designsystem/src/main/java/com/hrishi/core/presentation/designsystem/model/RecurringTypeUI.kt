package com.hrishi.core.presentation.designsystem.model

enum class RecurringTypeUI(val symbol: String, val title: String) {
    ONE_TIME("\uD83D\uDD04", "Does not repeat"),
    DAILY("\uD83D\uDD04", "Daily"),
    WEEKLY("\uD83D\uDD04", "Weekly on %s"),
    MONTHLY("\uD83D\uDD04", "Monthly on the %s"),
    YEARLY("\uD83D\uDD04", "Yearly on %s")
}