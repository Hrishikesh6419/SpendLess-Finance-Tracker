package com.spendless.dashboard.presentation.mapper

import com.hrishi.core.domain.model.DecimalSeparator
import com.hrishi.core.domain.model.ExpenseFormat
import com.hrishi.core.domain.model.ThousandsSeparator
import com.hrishi.core.presentation.designsystem.components.text_field.DecimalSeparatorUI
import com.hrishi.core.presentation.designsystem.components.text_field.ExpenseFormatUI
import com.hrishi.core.presentation.designsystem.components.text_field.ThousandsSeparatorUI

fun ExpenseFormat.toExpenseFormatUI(): ExpenseFormatUI {
    return when (this) {
        ExpenseFormat.MINUS_PREFIX -> ExpenseFormatUI.MINUS_SIGN
        ExpenseFormat.BRACKETS -> ExpenseFormatUI.PARENTHESES
    }
}

fun DecimalSeparator.toDecimalSeparatorUI(): DecimalSeparatorUI {
    return when (this) {
        DecimalSeparator.DOT -> DecimalSeparatorUI.DOT
        DecimalSeparator.COMMA -> DecimalSeparatorUI.COMMA
    }
}

fun ThousandsSeparator.toThousandsSeparatorUI(): ThousandsSeparatorUI {
    return when (this) {
        ThousandsSeparator.DOT -> ThousandsSeparatorUI.DOT
        ThousandsSeparator.COMMA -> ThousandsSeparatorUI.COMMA
        ThousandsSeparator.SPACE -> ThousandsSeparatorUI.SPACE
    }
}