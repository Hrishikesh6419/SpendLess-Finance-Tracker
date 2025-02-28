package com.hrishi.core.presentation.designsystem.model

import androidx.annotation.DrawableRes
import com.hrishi.presentation.designsystem.R

enum class TransactionTypeUI(@DrawableRes val iconRes: Int) {
    EXPENSE(R.drawable.ic_expense),
    INCOME(R.drawable.ic_income);

    fun displayText(): String {
        return when (this) {
            EXPENSE -> "Expense"
            INCOME -> "Income"
        }
    }
}