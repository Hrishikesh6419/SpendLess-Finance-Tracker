package com.hrishi.core.domain.export.repository

import com.hrishi.core.domain.export.model.ExportType
import com.hrishi.core.domain.utils.DataError
import com.hrishi.core.domain.utils.Result

interface ExportRepository {
    suspend fun exportTransactions(dateRange: ExportType, userId: Long): Result<Boolean, DataError>
}