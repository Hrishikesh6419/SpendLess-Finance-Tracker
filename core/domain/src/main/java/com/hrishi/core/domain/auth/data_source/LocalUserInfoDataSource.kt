package com.hrishi.core.domain.auth.data_source

import com.hrishi.core.domain.auth.model.UserInfo
import com.hrishi.core.domain.utils.DataError
import com.hrishi.core.domain.utils.Result
import kotlinx.coroutines.flow.Flow

interface LocalUserInfoDataSource {
    suspend fun upsertUser(userInfo: UserInfo): Result<Long, DataError>

    suspend fun getUser(userName: String): Result<UserInfo, DataError>

    fun getAllUsers(): Flow<Result<List<UserInfo>, DataError>>
}