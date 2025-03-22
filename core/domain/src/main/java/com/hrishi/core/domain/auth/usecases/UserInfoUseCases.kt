package com.hrishi.core.domain.auth.usecases

import com.hrishi.core.domain.auth.model.UserInfo
import com.hrishi.core.domain.auth.repository.UserInfoRepository
import com.hrishi.core.domain.utils.DataError
import com.hrishi.core.domain.utils.Result
import kotlinx.coroutines.flow.first

data class UserInfoUseCases(
    val getUserInfoUseCase: GetUserInfoUseCase,
    val areUsersPresentUseCase: AreUsersPresentUseCase,
)

class GetUserInfoUseCase(
    private val userInfoRepository: UserInfoRepository
) {
    suspend operator fun invoke(userName: String): Result<UserInfo, DataError> {

        return when (val result = userInfoRepository.getUser(userName = userName)) {
            is Result.Error -> Result.Error(result.error)
            is Result.Success -> {
                Result.Success(
                    UserInfo(
                        userId = result.data.userId,
                        username = result.data.username,
                        pin = result.data.pin
                    )
                )
            }
        }
    }
}

class AreUsersPresentUseCase(
    private val userInfoRepository: UserInfoRepository
) {
    suspend operator fun invoke(): Boolean {
        return userInfoRepository.getAllUsers()
            .first()
            .let { result ->
                when (result) {
                    is Result.Success -> result.data.isNotEmpty()
                    is Result.Error -> false
                }
            }
    }
}
