package com.hrishi.core.domain.auth.usecases

import com.hrishi.core.domain.auth.model.UserInfoDecrypted
import com.hrishi.core.domain.auth.repository.UserInfoRepository
import com.hrishi.core.domain.security.EncryptionService
import com.hrishi.core.domain.utils.DataError
import com.hrishi.core.domain.utils.Result

data class UserInfoUseCases(
    val getUserInfoUseCase: GetUserInfoUseCase
)

class GetUserInfoUseCase(
    private val userInfoRepository: UserInfoRepository,
    private val encryptionService: EncryptionService
) {
    suspend operator fun invoke(userName: String): Result<UserInfoDecrypted, DataError> {

        return when (val result = userInfoRepository.getUser(userName = userName)) {
            is Result.Error -> Result.Error(result.error)
            is Result.Success -> {
                val decryptedPin = encryptionService.decrypt(
                    encryptedData = result.data.encryptedPin,
                    iv = result.data.iv
                )
                Result.Success(
                    UserInfoDecrypted(
                        userId = result.data.userId,
                        username = result.data.username,
                        pin = decryptedPin
                    )
                )
            }
        }
    }
}