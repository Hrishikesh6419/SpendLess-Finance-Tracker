package com.spendless.core.database.auth.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.spendless.core.database.auth.entity.UserInfoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserInfoDao {

    @Insert
    suspend fun insertUser(userInfo: UserInfoEntity): Long

    @Query("SELECT * FROM user_info WHERE userId=:userId")
    suspend fun getUser(userId: Long): UserInfoEntity?

    @Query("SELECT * FROM user_info")
    fun getAllUsers(): Flow<List<UserInfoEntity>>
}