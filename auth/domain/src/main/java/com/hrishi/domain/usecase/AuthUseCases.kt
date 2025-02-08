package com.hrishi.domain.usecase

private const val MIN_USERNAME_LENGTH = 3
private const val MAX_USERNAME_LENGTH = 14

data class AuthUseCases(
    val isUsernameValidUseCase: IsUsernameValidUseCase,
    val isUsernameDuplicateUseCase: IsUsernameDuplicateUseCase
)

class IsUsernameValidUseCase {
    operator fun invoke(username: CharSequence): Boolean {
        return username.length in MIN_USERNAME_LENGTH..MAX_USERNAME_LENGTH &&
                username.all { it.isLetterOrDigit() }
    }
}

class IsUsernameDuplicateUseCase {
    operator fun invoke(username: CharSequence): Boolean {
        return false
    }
}