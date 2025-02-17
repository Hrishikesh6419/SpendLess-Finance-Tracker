package com.spendless.session_management.presentation.pin_prompt

sealed interface PinPromptEvent {
    data object OnSuccessPopBack : PinPromptEvent
    data object OnLogout : PinPromptEvent
    data object WrongPin : PinPromptEvent
}