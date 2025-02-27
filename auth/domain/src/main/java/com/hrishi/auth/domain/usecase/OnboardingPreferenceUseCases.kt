package com.hrishi.auth.domain.usecase

import com.hrishi.core.domain.model.DecimalSeparator
import com.hrishi.core.domain.model.ThousandsSeparator

data class OnboardingPreferenceUseCases(
    val validateSelectedPreferences: ValidateSelectedPreferences
)

class ValidateSelectedPreferences {
    operator fun invoke(
        decimalSeparator: DecimalSeparator,
        thousandsSeparator: ThousandsSeparator
    ): Boolean {
        return when {
            (decimalSeparator == DecimalSeparator.DOT && thousandsSeparator == ThousandsSeparator.DOT) ||
                    (decimalSeparator == DecimalSeparator.COMMA && thousandsSeparator == ThousandsSeparator.COMMA) -> false

            else -> true
        }
    }
}