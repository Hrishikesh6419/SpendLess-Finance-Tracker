package com.spendless.core.database.preferences.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.spendless.core.database.preferences.entity.UserPreferenceEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserPreferenceDao {

    @Upsert
    suspend fun upsertUserPreference(userPreferenceEntity: UserPreferenceEntity): Long

    @Query("SELECT * FROM user_preference WHERE userId=:userId")
    fun getUserPreference(userId: Long): Flow<UserPreferenceEntity?>
}