package com.spendless.dashboard.presentation.mapper

import com.hrishi.core.domain.model.DecimalSeparator
import com.hrishi.core.domain.model.TransactionCategory
import com.hrishi.core.domain.model.ExpenseFormat
import com.hrishi.core.domain.model.RecurringType
import com.hrishi.core.domain.model.ThousandsSeparator
import com.hrishi.core.domain.model.TransactionType
import com.hrishi.core.presentation.designsystem.components.text_field.DecimalSeparatorUI
import com.hrishi.core.presentation.designsystem.components.text_field.ExpenseFormatUI
import com.hrishi.core.presentation.designsystem.components.text_field.ThousandsSeparatorUI
import com.hrishi.core.presentation.designsystem.model.TransactionCategoryTypeUI
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

fun TransactionCategoryTypeUI.toTransactionCategory(): TransactionCategory {
    return when (this) {
        TransactionCategoryTypeUI.HOME -> TransactionCategory.HOME
        TransactionCategoryTypeUI.FOOD -> TransactionCategory.FOOD
        TransactionCategoryTypeUI.ENTERTAINMENT -> TransactionCategory.ENTERTAINMENT
        TransactionCategoryTypeUI.CLOTHING -> TransactionCategory.CLOTHING
        TransactionCategoryTypeUI.HEALTH -> TransactionCategory.HEALTH
        TransactionCategoryTypeUI.PERSONAL_CARE -> TransactionCategory.PERSONAL_CARE
        TransactionCategoryTypeUI.TRANSPORTATION -> TransactionCategory.TRANSPORTATION
        TransactionCategoryTypeUI.EDUCATION -> TransactionCategory.EDUCATION
        TransactionCategoryTypeUI.SAVINGS -> TransactionCategory.SAVINGS
        TransactionCategoryTypeUI.OTHER -> TransactionCategory.OTHER
        TransactionCategoryTypeUI.INCOME -> TransactionCategory.INCOME
    }
}

fun TransactionCategory.toTransactionCategoryUI(): TransactionCategoryTypeUI {
    return when (this) {
        TransactionCategory.HOME -> TransactionCategoryTypeUI.HOME
        TransactionCategory.FOOD -> TransactionCategoryTypeUI.FOOD
        TransactionCategory.ENTERTAINMENT -> TransactionCategoryTypeUI.ENTERTAINMENT
        TransactionCategory.CLOTHING -> TransactionCategoryTypeUI.CLOTHING
        TransactionCategory.HEALTH -> TransactionCategoryTypeUI.HEALTH
        TransactionCategory.PERSONAL_CARE -> TransactionCategoryTypeUI.PERSONAL_CARE
        TransactionCategory.TRANSPORTATION -> TransactionCategoryTypeUI.TRANSPORTATION
        TransactionCategory.EDUCATION -> TransactionCategoryTypeUI.EDUCATION
        TransactionCategory.SAVINGS -> TransactionCategoryTypeUI.SAVINGS
        TransactionCategory.OTHER -> TransactionCategoryTypeUI.OTHER
        TransactionCategory.INCOME -> TransactionCategoryTypeUI.INCOME
    }
}

fun RecurringTypeUI.toRecurringType(): RecurringType {
    return when (this) {
        RecurringTypeUI.ONE_TIME -> RecurringType.ONE_TIME
        RecurringTypeUI.DAILY -> RecurringType.DAILY
        RecurringTypeUI.WEEKLY -> RecurringType.WEEKLY
        RecurringTypeUI.MONTHLY -> RecurringType.MONTHLY
        RecurringTypeUI.YEARLY -> RecurringType.YEARLY
    }
}

fun RecurringType.toRecurringTypeUI(): RecurringTypeUI {
    return when (this) {
        RecurringType.ONE_TIME -> RecurringTypeUI.ONE_TIME
        RecurringType.DAILY -> RecurringTypeUI.DAILY
        RecurringType.WEEKLY -> RecurringTypeUI.WEEKLY
        RecurringType.MONTHLY -> RecurringTypeUI.MONTHLY
        RecurringType.YEARLY -> RecurringTypeUI.YEARLY
    }
}