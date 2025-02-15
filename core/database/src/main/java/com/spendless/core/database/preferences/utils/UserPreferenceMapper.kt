package com.spendless.core.database.preferences.utils

import com.hrishi.core.domain.model.Currency
import com.hrishi.core.domain.model.DecimalSeparator
import com.hrishi.core.domain.model.ExpenseFormat
import com.hrishi.core.domain.model.ThousandsSeparator
import com.hrishi.core.domain.preference.model.UserPreferences
import com.spendless.core.database.preferences.entity.UserPreferenceEntity

fun UserPreferenceEntity.toUserPreferences(): UserPreferences {
    return UserPreferences(
        userId = userId,
        expenseFormat = ExpenseFormat.valueOf(expenseFormat.name),
        currency = Currency.valueOf(currency.name),
        decimalSeparator = DecimalSeparator.valueOf(decimalSeparator.name),
        thousandsSeparator = ThousandsSeparator.valueOf(thousandsSeparator.name)
    )
}

fun UserPreferences.toUserPreferenceEntity(): UserPreferenceEntity {
    return UserPreferenceEntity(
        userId = userId,
        expenseFormat = expenseFormat,
        currency = currency,
        decimalSeparator = decimalSeparator,
        thousandsSeparator = thousandsSeparator
    )
}