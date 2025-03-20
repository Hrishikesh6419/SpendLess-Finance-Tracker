package com.hrishi.auth.domain.usecase

import com.hrishi.core.domain.auth.repository.UserInfoRepository
import com.hrishi.core.domain.utils.DataError
import com.hrishi.core.domain.utils.Result

data class LoginUseCases(
    val isUsernameValidUseCase: IsUsernameValidUseCase,
    val initiateLoginUseCase: InitiateLoginUseCase
)

class IsUsernameValidUseCase {
    operator fun invoke(username: CharSequence): Boolean {
        return username.length in MIN_USERNAME_LENGTH..MAX_USERNAME_LENGTH &&
                username.all { it.isLetterOrDigit() }
    }

    companion object {
        private const val MIN_USERNAME_LENGTH = 3
        private const val MAX_USERNAME_LENGTH = 14
    }
}

class InitiateLoginUseCase(
    private val userInfoRepository: UserInfoRepository
) {
    suspend operator fun invoke(username: String, enteredPin: String): Result<Long, DataError> {
        return when (val userResult = userInfoRepository.getUser(username)) {
            is Result.Success -> {
                val storedPin = userResult.data.pin
                if (enteredPin == storedPin) {
                    Result.Success(userResult.data.userId ?: 0L)
                } else {
                    Result.Error(DataError.Local.INVALID_CREDENTIALS)
                }
            }

            is Result.Error -> Result.Error(DataError.Local.INVALID_CREDENTIALS)
        }
    }
}