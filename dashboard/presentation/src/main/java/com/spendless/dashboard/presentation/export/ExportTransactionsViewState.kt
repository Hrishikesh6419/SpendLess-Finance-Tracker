package com.spendless.dashboard.presentation.export

import com.hrishi.core.domain.model.ExportType

data class ExportTransactionsViewState(
    val exportType: ExportType = ExportType.LAST_THREE_MONTH
)