package com.spendless.dashboard.presentation.dashboard

sealed interface DashboardAction {
    data object NavigationClick : DashboardAction
    data object OnSettingsClicked : DashboardAction
    data object OnShowAllTransactionsClicked : DashboardAction
    data class UpdatedBottomSheet(val showSheet: Boolean) : DashboardAction
    data class UpdateExportBottomSheet(val showSheet: Boolean) : DashboardAction
    data class OnCardClicked(val transactionId: Long?) : DashboardAction
}