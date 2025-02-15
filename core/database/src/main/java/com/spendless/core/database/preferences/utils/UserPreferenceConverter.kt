package com.spendless.core.database.preferences.utils

import androidx.room.TypeConverter
import com.hrishi.core.domain.model.Currency
import com.hrishi.core.domain.model.DecimalSeparator
import com.hrishi.core.domain.model.ExpenseFormat
import com.hrishi.core.domain.model.ThousandsSeparator

class UserPreferenceConverters {

    @TypeConverter
    fun fromDecimalSeparator(value: DecimalSeparator): String {
        return value.name
    }

    @TypeConverter
    fun toDecimalSeparator(value: String): DecimalSeparator {
        return DecimalSeparator.valueOf(value)
    }

    @TypeConverter
    fun fromThousandsSeparator(value: ThousandsSeparator): String {
        return value.name
    }

    @TypeConverter
    fun toThousandsSeparator(value: String): ThousandsSeparator {
        return ThousandsSeparator.valueOf(value)
    }

    @TypeConverter
    fun fromExpenseFormat(value: ExpenseFormat): String {
        return value.name
    }

    @TypeConverter
    fun toExpenseFormat(value: String): ExpenseFormat {
        return ExpenseFormat.valueOf(value)
    }

    @TypeConverter
    fun fromCurrency(value: Currency): String {
        return value.name
    }

    @TypeConverter
    fun toCurrency(value: String): Currency {
        return Currency.valueOf(value)
    }
}