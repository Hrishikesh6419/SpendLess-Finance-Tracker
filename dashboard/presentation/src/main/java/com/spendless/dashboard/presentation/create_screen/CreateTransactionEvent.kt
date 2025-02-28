package com.spendless.dashboard.presentation.create_screen

sealed interface CreateTransactionEvent {
    data object CloseBottomSheet : CreateTransactionEvent
}