package com.spendless.dashboard.presentation.export

import com.hrishi.core.domain.model.ExportType

sealed interface ExportTransactionsAction {
    data class OnExportTypeUpdated(val exportType: ExportType) : ExportTransactionsAction
    data object OnExportClicked : ExportTransactionsAction
    data object OnDismissClicked : ExportTransactionsAction
}