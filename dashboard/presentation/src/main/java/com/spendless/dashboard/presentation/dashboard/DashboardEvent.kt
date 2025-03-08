package com.spendless.dashboard.presentation.dashboard

sealed interface DashboardEvent {
    data object NavigateToSettings : DashboardEvent
    data object NavigateToAllTransactions : DashboardEvent
}