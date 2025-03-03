package com.spendless.dashboard.presentation.all_transactions

sealed interface AllTransactionsAction {
    data object OnClickBackButton : AllTransactionsAction
    data class UpdatedBottomSheet(val showSheet: Boolean) : AllTransactionsAction
    data class OnCardClicked(val transactionId: Long?) : AllTransactionsAction
}