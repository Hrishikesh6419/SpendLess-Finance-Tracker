package com.hrishi.auth.domain.usecase

import com.hrishi.auth.domain.repository.UserInfoRepository
import com.hrishi.core.domain.auth.model.UserInfo
import com.hrishi.core.domain.utils.DataError
import com.hrishi.core.domain.utils.Result

data class RegisterUseCases(
    val registerUserUseCase: RegisterUserUseCase
)

class RegisterUserUseCase(private val userInfoRepository: UserInfoRepository) {
    suspend operator fun invoke(userInfo: UserInfo): Result<Long, DataError> {
        return userInfoRepository.registerUser(userInfo)
    }
}