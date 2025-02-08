package com.hrishi.auth.presentation.create_pin

sealed interface CreatePinAction {
    data class OnPinUpdate(val pin: String) : CreatePinAction
    data class OnNumberPressed(val number: Int) : CreatePinAction
    data object OnDeletePressed : CreatePinAction
}