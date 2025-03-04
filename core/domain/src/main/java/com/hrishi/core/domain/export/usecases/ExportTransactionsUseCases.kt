package com.hrishi.core.domain.export.usecases

import com.hrishi.core.domain.export.model.ExportType
import com.hrishi.core.domain.export.repository.ExportRepository
import com.hrishi.core.domain.utils.DataError
import com.hrishi.core.domain.utils.Result

data class ExportTransactionsUseCases(
    val exportTransactionUseCase: ExportTransactionUseCase
)

class ExportTransactionUseCase(private val exportRepository: ExportRepository) {
    suspend operator fun invoke(userId: Long, exportType: ExportType): Result<Boolean, DataError> {
        return exportRepository.exportTransactions(
            dateRange = exportType,
            userId = userId
        )
    }
}