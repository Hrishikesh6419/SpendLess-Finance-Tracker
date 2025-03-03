package com.spendless.dashboard.presentation.all_transactions

sealed interface AllTransactionsEvent {
    data object NavigateBack : AllTransactionsEvent
}