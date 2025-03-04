package com.hrishi.core.data.export.repository

import android.os.Environment
import com.hrishi.core.domain.export.model.ExportType
import com.hrishi.core.domain.export.repository.ExportRepository
import com.hrishi.core.domain.transactions.model.Transaction
import com.hrishi.core.domain.transactions.repository.TransactionRepository
import com.hrishi.core.domain.utils.DataError
import com.hrishi.core.domain.utils.Result
import com.hrishi.core.domain.utils.toISODateString
import java.io.File
import java.io.FileWriter
import java.io.IOException

class ExportRepositoryImpl(
    private val transactionRepository: TransactionRepository
) : ExportRepository {

    override suspend fun exportTransactions(
        dateRange: ExportType,
        userId: Long
    ): Result<Boolean, DataError> {
        val (startDate, endDate) = dateRange.getDateRange()

        return try {
            val transactionsResult =
                transactionRepository.getTransactionsForDateRange(userId, startDate, endDate)

            when (transactionsResult) {
                is Result.Success -> {
                    val exportResult = writeTransactionsToCsv(transactionsResult.data)
                    if (exportResult) {
                        Result.Success(true)
                    } else {
                        Result.Error(DataError.Local.EXPORT_FAILED)
                    }
                }

                is Result.Error -> Result.Error(transactionsResult.error)
            }
        } catch (e: Exception) {
            Result.Error(DataError.Local.UNKNOWN_DATABASE_ERROR)
        }
    }

    private fun writeTransactionsToCsv(transactions: List<Transaction>): Boolean {
        val downloadsDir =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        if (!downloadsDir.exists()) downloadsDir.mkdirs()

        val csvFile = File(downloadsDir, "Spendless_transactions.csv")

        return try {
            FileWriter(csvFile).use { writer ->
                val headers = listOf(
                    "Transaction Type",
                    "Amount",
                    "Date",
                    "Transaction Name",
                    "Transaction Category",
                    "Note"
                )
                writer.appendLine(headers.joinToString(",") { escapeCsv(it) })

                transactions.forEach { transaction ->
                    val csvLine = buildCsvLine(transaction)
                    writer.appendLine(csvLine)
                }
                writer.flush()
            }
            true
        } catch (e: IOException) {
            false
        }
    }


    private fun buildCsvLine(transaction: Transaction): String {
        return listOf(
            transaction.transactionType.displayName,
            transaction.amount.toString(),
            transaction.transactionDate.toISODateString(),
            transaction.transactionName,
            transaction.transactionCategory.displayName,
            transaction.note.orEmpty()
        ).joinToString(",") { escapeCsv(it) }
    }

    private fun escapeCsv(value: String): String {
        val escapedValue = value.replace("\"", "\"\"")
        return "\"$escapedValue\""
    }
}
