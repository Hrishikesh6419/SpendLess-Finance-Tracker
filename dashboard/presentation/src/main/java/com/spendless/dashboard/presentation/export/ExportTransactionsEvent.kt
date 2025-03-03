package com.spendless.dashboard.presentation.export

sealed interface ExportTransactionsEvent {
    data object ExportSuccessful : ExportTransactionsEvent
    data object CloseBottomSheet : ExportTransactionsEvent
}