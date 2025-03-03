package com.spendless.core.database.transactions.utils

import androidx.room.TypeConverter
import com.hrishi.core.domain.model.TransactionCategory
import com.hrishi.core.domain.utils.CalendarUtils
import java.math.BigDecimal
import java.time.LocalDateTime

object TransactionConverters {
    @TypeConverter
    fun fromBigDecimal(value: BigDecimal?): String? {
        return value?.toPlainString()
    }

    @TypeConverter
    fun toBigDecimal(value: String?): BigDecimal? {
        return try {
            value?.let { BigDecimal(it) }
        } catch (e: NumberFormatException) {
            null
        }
    }

    @TypeConverter
    fun fromLocalDateTime(value: LocalDateTime?): Long? {
        return value?.let { CalendarUtils.toEpochMillis(it) }
    }

    @TypeConverter
    fun toLocalDateTime(value: Long?): LocalDateTime? {
        return value?.let { CalendarUtils.epochToLocalDateTime(it) }
    }

    @TypeConverter
    fun fromTransactionCategory(category: TransactionCategory): String {
        return category.name
    }

    @TypeConverter
    fun toTransactionCategory(category: String): TransactionCategory {
        return TransactionCategory.valueOf(category)
    }
}
