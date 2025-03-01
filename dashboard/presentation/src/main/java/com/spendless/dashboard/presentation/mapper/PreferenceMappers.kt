package com.spendless.dashboard.presentation.mapper

import com.hrishi.core.domain.model.DecimalSeparator
import com.hrishi.core.domain.model.ExpenseCategory
import com.hrishi.core.domain.model.ExpenseFormat
import com.hrishi.core.domain.model.RecurringType
import com.hrishi.core.domain.model.ThousandsSeparator
import com.hrishi.core.domain.model.TransactionType
import com.hrishi.core.presentation.designsystem.components.text_field.DecimalSeparatorUI
import com.hrishi.core.presentation.designsystem.components.text_field.ExpenseFormatUI
import com.hrishi.core.presentation.designsystem.components.text_field.ThousandsSeparatorUI
import com.hrishi.core.presentation.designsystem.model.ExpenseCategoryTypeUI
import com.hrishi.core.presentation.designsystem.model.RecurringTypeUI
import com.hrishi.core.presentation.designsystem.model.TransactionTypeUI

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

fun TransactionTypeUI.toTransactionType(): TransactionType {
    return when (this) {
        TransactionTypeUI.EXPENSE -> TransactionType.EXPENSE
        TransactionTypeUI.INCOME -> TransactionType.INCOME
    }
}

fun ExpenseFormatUI.toExpenseFormat(): ExpenseFormat {
    return when (this) {
        ExpenseFormatUI.MINUS_SIGN -> ExpenseFormat.MINUS_PREFIX
        ExpenseFormatUI.PARENTHESES -> ExpenseFormat.BRACKETS
    }
}

fun DecimalSeparatorUI.toDecimalSeparator(): DecimalSeparator {
    return when (this) {
        DecimalSeparatorUI.DOT -> DecimalSeparator.DOT
        DecimalSeparatorUI.COMMA -> DecimalSeparator.COMMA
    }
}

fun ThousandsSeparatorUI.toThousandsSeparator(): ThousandsSeparator {
    return when (this) {
        ThousandsSeparatorUI.DOT -> ThousandsSeparator.DOT
        ThousandsSeparatorUI.COMMA -> ThousandsSeparator.COMMA
        ThousandsSeparatorUI.SPACE -> ThousandsSeparator.SPACE
    }
}

fun ExpenseCategoryTypeUI.toExpenseCategory(): ExpenseCategory {
    return when (this) {
        ExpenseCategoryTypeUI.HOME -> ExpenseCategory.HOME
        ExpenseCategoryTypeUI.FOOD -> ExpenseCategory.FOOD
        ExpenseCategoryTypeUI.ENTERTAINMENT -> ExpenseCategory.ENTERTAINMENT
        ExpenseCategoryTypeUI.CLOTHING -> ExpenseCategory.CLOTHING
        ExpenseCategoryTypeUI.HEALTH -> ExpenseCategory.HEALTH
        ExpenseCategoryTypeUI.PERSONAL_CARE -> ExpenseCategory.PERSONAL_CARE
        ExpenseCategoryTypeUI.TRANSPORTATION -> ExpenseCategory.TRANSPORTATION
        ExpenseCategoryTypeUI.EDUCATION -> ExpenseCategory.EDUCATION
        ExpenseCategoryTypeUI.SAVINGS -> ExpenseCategory.SAVINGS
        ExpenseCategoryTypeUI.OTHER -> ExpenseCategory.OTHER
    }
}

fun RecurringTypeUI.toRecurringType(): RecurringType {
    return when (this) {
        RecurringTypeUI.ONE_TIME -> RecurringType.ONE_TIME
        RecurringTypeUI.DAILY -> RecurringType.DAILY
        RecurringTypeUI.WEEKLY -> RecurringType.WEEKLY
        RecurringTypeUI.WEEKLY_ON_THIS_DAY -> RecurringType.WEEKLY_ON_THIS_DAY
        RecurringTypeUI.MONTHLY_ON_THIS_DAY -> RecurringType.MONTHLY_ON_THIS_DAY
        RecurringTypeUI.YEARLY_ON_THIS_DAY -> RecurringType.YEARLY_ON_THIS_DAY
    }
}

fun RecurringType.toRecurringTypeUI(): RecurringTypeUI {
    return when (this) {
        RecurringType.ONE_TIME -> RecurringTypeUI.ONE_TIME
        RecurringType.DAILY -> RecurringTypeUI.DAILY
        RecurringType.WEEKLY -> RecurringTypeUI.WEEKLY
        RecurringType.WEEKLY_ON_THIS_DAY -> RecurringTypeUI.WEEKLY_ON_THIS_DAY
        RecurringType.MONTHLY_ON_THIS_DAY -> RecurringTypeUI.MONTHLY_ON_THIS_DAY
        RecurringType.YEARLY_ON_THIS_DAY -> RecurringTypeUI.YEARLY_ON_THIS_DAY
    }
}