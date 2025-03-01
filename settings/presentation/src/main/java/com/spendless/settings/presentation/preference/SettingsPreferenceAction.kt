package com.spendless.settings.presentation.preference

import com.hrishi.core.domain.model.Currency
import com.hrishi.core.domain.model.DecimalSeparator
import com.hrishi.core.domain.model.ExpenseFormat
import com.hrishi.core.domain.model.ThousandsSeparator

sealed interface SettingsPreferencesAction {
    data class OnExpenseFormatUpdate(val format: ExpenseFormat) : SettingsPreferencesAction
    data class OnCurrencyUpdate(val currency: Currency) : SettingsPreferencesAction
    data class OnDecimalSeparatorUpdate(val format: DecimalSeparator) : SettingsPreferencesAction
    data class OnThousandsSeparatorUpdate(val format: ThousandsSeparator) :
        SettingsPreferencesAction

    data object OnBackClicked : SettingsPreferencesAction
    data object OnSaveClicked : SettingsPreferencesAction
}