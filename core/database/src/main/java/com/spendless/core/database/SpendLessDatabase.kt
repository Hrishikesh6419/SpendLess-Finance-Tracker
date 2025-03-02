package com.spendless.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.spendless.core.database.auth.dao.UserInfoDao
import com.spendless.core.database.auth.entity.UserInfoEntity
import com.spendless.core.database.preferences.dao.UserPreferenceDao
import com.spendless.core.database.preferences.entity.UserPreferenceEntity
import com.spendless.core.database.transactions.dao.TransactionsDao
import com.spendless.core.database.transactions.entity.TransactionEntity
import com.spendless.core.database.transactions.utils.TransactionConverters

@Database(
    entities = [
        UserInfoEntity::class,
        UserPreferenceEntity::class,
        TransactionEntity::class
    ],
    version = 1
)
@TypeConverters(TransactionConverters::class)
abstract class SpendLessDatabase : RoomDatabase() {
    abstract val userInfoDao: UserInfoDao
    abstract val userPreferenceDao: UserPreferenceDao
    abstract val transactionsDao: TransactionsDao
}