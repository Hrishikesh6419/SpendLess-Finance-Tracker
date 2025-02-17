package com.spendless.session_management.presentation.pin_prompt

sealed interface PinPromptAction {
    data class OnNumberPressed(val number: Int) : PinPromptAction
    data object OnDeletePressed : PinPromptAction
    data object OnLogoutClicked : PinPromptAction
}