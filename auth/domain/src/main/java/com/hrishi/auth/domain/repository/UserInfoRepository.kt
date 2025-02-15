package com.hrishi.auth.domain.repository

import com.hrishi.core.domain.auth.model.UserInfo
import com.hrishi.core.domain.utils.DataError
import com.hrishi.core.domain.utils.Result
import kotlinx.coroutines.flow.Flow

interface UserInfoRepository {

    suspend fun registerUser(userInfo: UserInfo): Result<Long, DataError>

    suspend fun getUser(userId: Long): Result<UserInfo, DataError>

    fun getAllUsers(): Flow<Result<List<UserInfo>, DataError>>
}
