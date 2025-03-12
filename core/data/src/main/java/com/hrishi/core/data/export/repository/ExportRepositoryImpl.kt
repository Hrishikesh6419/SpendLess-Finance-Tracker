package com.hrishi.core.data.export.repository

import android.content.ContentValues
import android.content.Context
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import com.hrishi.core.domain.export.model.ExportType
import com.hrishi.core.domain.export.repository.ExportRepository
import com.hrishi.core.domain.formatting.NumberFormatter
import com.hrishi.core.domain.model.RecurringType
import com.hrishi.core.domain.preference.model.UserPreferences
import com.hrishi.core.domain.transactions.model.Transaction
import com.hrishi.core.domain.transactions.repository.TransactionRepository
import com.hrishi.core.domain.utils.DataError
import com.hrishi.core.domain.utils.Result
import com.hrishi.core.domain.utils.toISODateString
import timber.log.Timber

class ExportRepositoryImpl(
    private val context: Context,
    private val transactionRepository: TransactionRepository
) : ExportRepository {

    companion object {
        private const val FILE_NAME = "Spendless_transactions.csv"
        private const val MIME_TYPE = "text/csv"
        private val CSV_HEADERS = listOf(
            "Transaction Type",
            "Amount",
            "Date",
            "Transaction Name",
            "Transaction Category",
            "Recurring Type",
            "Start Date",
            "Next Recurring Date",
            "Note"
        ).joinToString(",")

        private const val EXPORT_TAG = "ExportDebug"
        private const val NOT_APPLICABLE = "N/A"
    }

    override suspend fun exportTransactions(
        dateRange: ExportType,
        userId: Long,
        userPreference: UserPreferences
    ): Result<Boolean, DataError> {
        val (startDate, endDate) = dateRange.getDateRange()

        return try {
            val transactionsResult =
                transactionRepository.getTransactionsForDateRange(userId, startDate, endDate)

            when (transactionsResult) {
                is Result.Success -> {
                    val exportResult =
                        writeTransactionsToCsv(transactionsResult.data, userPreference)
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

    private fun writeTransactionsToCsv(
        transactions: List<Transaction>,
        userPreference: UserPreferences
    ): Boolean {

        if (transactions.isEmpty()) {
            Timber.tag(EXPORT_TAG).e("No transactions to export!")
            return false
        }

        val csvContent = buildString {
            appendLine(CSV_HEADERS)
            transactions.joinTo(this, separator = "\n") {
                buildCsvLine(it, userPreference)
            }
        }

        val resolver = context.contentResolver

        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, FILE_NAME)
            put(MediaStore.MediaColumns.MIME_TYPE, MIME_TYPE)
            put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
        }

        val contentUri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            MediaStore.Downloads.EXTERNAL_CONTENT_URI
        } else {
            MediaStore.Files.getContentUri("external")
        }

        return runCatching {
            resolver.insert(contentUri, contentValues)?.let { uri ->
                resolver.openOutputStream(uri)?.use { outputStream ->
                    outputStream.write(csvContent.toByteArray())
                    Timber.tag(EXPORT_TAG).d("CSV successfully written to $uri")
                    true
                }
            } ?: false
        }.getOrElse {
            Timber.tag(EXPORT_TAG).e(it, "Failed to write CSV")
            false
        }
    }

    private fun buildCsvLine(transaction: Transaction, userPreference: UserPreferences): String {
        val recurringTypeTitle =
            transaction.recurringType.exportTitle(transaction.recurringStartDate)
        val startDate = if (transaction.recurringType == RecurringType.ONE_TIME) {
            NOT_APPLICABLE
        } else {
            transaction.recurringStartDate.toISODateString()
        }
        val nextRecurring = if (transaction.recurringType == RecurringType.ONE_TIME ||
            transaction.nextRecurringDate == null
        ) NOT_APPLICABLE else {
            transaction.nextRecurringDate?.toISODateString() ?: "N/A"
        }

        return listOf(
            transaction.transactionType.displayName,
            NumberFormatter.formatAmount(
                amount = transaction.amount,
                preferences = userPreference
            ),
            transaction.transactionDate.toISODateString(),
            transaction.transactionName,
            transaction.transactionCategory.displayName,
            recurringTypeTitle,
            startDate,
            nextRecurring,
            transaction.note.orEmpty(),
        ).joinToString(",") { escapeCsv(it) }
    }

    private fun escapeCsv(value: String): String {
        val escapedValue = value.replace("\"", "\"\"")
        return "\"$escapedValue\""
    }
}