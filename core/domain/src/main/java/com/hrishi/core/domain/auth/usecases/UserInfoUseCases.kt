package com.hrishi.core.domain.auth.usecases

import com.hrishi.core.domain.auth.model.UserInfo
import com.hrishi.core.domain.auth.repository.UserInfoRepository
import com.hrishi.core.domain.utils.DataError
import com.hrishi.core.domain.utils.Result

data class UserInfoUseCases(
    val getUserInfoUseCase: GetUserInfoUseCase
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