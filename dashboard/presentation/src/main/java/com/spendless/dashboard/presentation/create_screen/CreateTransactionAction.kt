package com.spendless.dashboard.presentation.create_screen

import com.hrishi.core.presentation.designsystem.model.TransactionTypeUI
import java.math.BigDecimal

sealed interface CreateTransactionAction {
    data class OnTransactionTypeChanged(val transactionType: TransactionTypeUI) :
        CreateTransactionAction

    data class OnTransactionNameUpdated(val transactionName: String) : CreateTransactionAction
    data class OnAmountUpdated(val amount: BigDecimal) : CreateTransactionAction
    data object OnCreateClicked : CreateTransactionAction
    data object OnCategoryClicked : CreateTransactionAction
    data object OnFrequencyClicked : CreateTransactionAction
}