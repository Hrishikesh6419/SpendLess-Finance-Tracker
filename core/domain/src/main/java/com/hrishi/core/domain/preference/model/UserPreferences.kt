package com.hrishi.core.domain.preference.model

import com.hrishi.core.domain.model.Currency
import com.hrishi.core.domain.model.DecimalSeparator
import com.hrishi.core.domain.model.ExpenseFormat
import com.hrishi.core.domain.model.LockoutDuration
import com.hrishi.core.domain.model.SessionDuration
import com.hrishi.core.domain.model.ThousandsSeparator

data class UserPreferences(
    val userId: Long,
    val expenseFormat: ExpenseFormat,
    val currency: Currency,
    val decimalSeparator: DecimalSeparator,
    val thousandsSeparator: ThousandsSeparator,
    val isBiometricEnabled: Boolean,
    val sessionDuration: SessionDuration,
    val lockOutDuration: LockoutDuration
)