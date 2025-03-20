package com.hrishi.auth.domain.usecase

data class CreatePinUseCases(
    val appendDigitUseCase: AppendDigitUseCase,
    val deleteDigitUseCase: DeleteDigitUseCase,
    val validatePinMatchUseCase: ValidatePinMatchUseCase
)

class AppendDigitUseCase {
    operator fun invoke(currentPin: String, digit: String): String {
        return if (currentPin.length < MAX_PIN_LENGTH) currentPin + digit else currentPin
    }

    companion object {
        private const val MAX_PIN_LENGTH = 5
    }
}

class DeleteDigitUseCase {
    operator fun invoke(currentPin: String): String {
        return currentPin.dropLast(1)
    }
}

class ValidatePinMatchUseCase {
    operator fun invoke(enteredPin: String, targetPin: String?): Boolean {
        return targetPin != null && enteredPin.equals(targetPin, ignoreCase = true)
    }
}