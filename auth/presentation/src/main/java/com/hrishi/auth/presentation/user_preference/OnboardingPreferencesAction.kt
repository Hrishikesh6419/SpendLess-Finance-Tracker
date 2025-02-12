package com.hrishi.auth.presentation.user_preference

import com.hrishi.domain.model.Currency
import com.hrishi.domain.model.DecimalSeparator
import com.hrishi.domain.model.ExpenseFormat
import com.hrishi.domain.model.ThousandsSeparator

sealed interface OnboardingPreferencesAction {
    data class OnExpenseFormatUpdate(val format: ExpenseFormat) : OnboardingPreferencesAction
    data class OnCurrencyUpdate(val currency: Currency) : OnboardingPreferencesAction
    data class OnDecimalSeparatorUpdate(val format: DecimalSeparator) : OnboardingPreferencesAction
    data class OnThousandsSeparatorUpdate(val format: ThousandsSeparator) :
        OnboardingPreferencesAction
    data object OnBackClicked : OnboardingPreferencesAction
    data object OnStartClicked : OnboardingPreferencesAction
}