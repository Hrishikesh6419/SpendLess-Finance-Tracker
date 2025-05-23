package com.hrishi.core.data.repository

import com.hrishi.core.domain.auth.data_source.LocalUserInfoDataSource
import com.hrishi.core.domain.auth.model.UserInfo
import com.hrishi.core.domain.auth.repository.UserInfoRepository
import com.hrishi.core.domain.utils.DataError
import com.hrishi.core.domain.utils.Result
import kotlinx.coroutines.flow.Flow

class UserInfoRepositoryImpl(
    private val localUserInfoDataSource: LocalUserInfoDataSource
) : UserInfoRepository {

    override suspend fun registerUser(userInfo: UserInfo): Result<Long, DataError> {
        return localUserInfoDataSource.upsertUser(userInfo)
    }

    override suspend fun getUser(userName: String): Result<UserInfo, DataError> {
        return localUserInfoDataSource.getUser(userName)
    }

    override fun getAllUsers(): Flow<Result<List<UserInfo>, DataError>> {
        return localUserInfoDataSource.getAllUsers()
    }
}