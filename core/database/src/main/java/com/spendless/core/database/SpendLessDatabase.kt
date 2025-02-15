package com.spendless.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.spendless.core.database.auth.dao.UserInfoDao
import com.spendless.core.database.auth.entity.UserInfoEntity
import com.spendless.core.database.preferences.dao.UserPreferenceDao
import com.spendless.core.database.preferences.entity.UserPreferenceEntity

@Database(
    entities = [
        UserInfoEntity::class,
        UserPreferenceEntity::class
    ],
    version = 1
)
abstract class SpendLessDatabase : RoomDatabase() {
    abstract val userInfoDao: UserInfoDao
    abstract val userPreferenceDao: UserPreferenceDao
}