package com.spendless.core.database.auth.data_source

import android.database.sqlite.SQLiteConstraintException
import com.hrishi.core.domain.auth.data_source.LocalUserInfoDataSource
import com.hrishi.core.domain.auth.model.UserInfo
import com.hrishi.core.domain.utils.DataError
import com.hrishi.core.domain.utils.Result
import com.spendless.core.database.auth.dao.UserInfoDao
import com.spendless.core.database.auth.utils.toUserEntity
import com.spendless.core.database.auth.utils.toUserInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RoomLocalUserInfoDataSource(
    private val userInfoDao: UserInfoDao
) : LocalUserInfoDataSource {
    override suspend fun upsertUser(userInfo: UserInfo): Result<Long, DataError> {
        return try {
            val userId = userInfoDao.insertUser(userInfo.toUserEntity())

            when {
                userId > 0 -> Result.Success(userId)
                userId == -1L -> Result.Error(DataError.Local.INSERT_USER_ERROR)
                else -> Result.Error(DataError.Local.UNKNOWN_DATABASE_ERROR)
            }
        } catch (e: SQLiteConstraintException) {
            Result.Error(DataError.Local.DUPLICATE_USER_ERROR)
        } catch (e: Exception) {
            Result.Error(DataError.Local.UNKNOWN_DATABASE_ERROR)
        }
    }

    override suspend fun getUser(userId: Long): Result<UserInfo, DataError> {
        val userEntity = userInfoDao.getUser(userId)
        return userEntity?.let {
            Result.Success(it.toUserInfo())
        } ?: Result.Error(DataError.Local.USER_FETCH_ERROR)
    }

    override fun getAllUsers(): Flow<Result<List<UserInfo>, DataError>> {
        return userInfoDao.getAllUsers()
            .map { userEntities ->
                if (!userEntities.isNullOrEmpty()) {
                    Result.Success(userEntities.map { it.toUserInfo() })
                } else {
                    Result.Error(DataError.Local.USER_FETCH_ERROR)
                }
            }
    }
}