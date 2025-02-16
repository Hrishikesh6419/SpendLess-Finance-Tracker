package com.spendless.dashboard.presentation.dashboard

sealed interface DashboardEvent {
    data object NavigateTest : DashboardEvent
}