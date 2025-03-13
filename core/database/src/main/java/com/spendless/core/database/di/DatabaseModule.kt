package com.spendless.core.database.di

import androidx.room.Room
import com.hrishi.core.domain.auth.data_source.LocalUserInfoDataSource
import com.hrishi.core.domain.preference.data_source.LocalPreferencesDataSource
import com.hrishi.core.domain.transactions.data_source.LocalTransactionDataSource
import com.spendless.core.database.BuildConfig
import com.spendless.core.database.SpendLessDatabase
import com.spendless.core.database.auth.data_source.RoomLocalUserInfoDataSource
import com.spendless.core.database.preferences.data_source.RoomLocalUserPreferenceDataSource
import com.spendless.core.database.security.DatabaseSecurity
import com.spendless.core.database.transactions.data_source.RoomTransactionDataSource
import net.sqlcipher.database.SQLiteDatabase
import net.sqlcipher.database.SupportFactory
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val databaseModule = module {
    single {
        val isEncrypted = BuildConfig.IS_DATABASE_ENCRYPTION_ENABLED
        val dbName = "spendless.db"

        val builder = Room.databaseBuilder(
            androidApplication(),
            SpendLessDatabase::class.java,
            dbName
        )

        if (isEncrypted) {
            val passphrase = DatabaseSecurity.getDatabasePassphrase(androidApplication())
            val factory = SupportFactory(SQLiteDatabase.getBytes(passphrase.toCharArray()))
            builder.openHelperFactory(factory)
        }

        builder.build()
    }
    single { get<SpendLessDatabase>().userInfoDao }
    single { get<SpendLessDatabase>().userPreferenceDao }
    single { get<SpendLessDatabase>().transactionsDao }

    singleOf(::RoomLocalUserInfoDataSource).bind<LocalUserInfoDataSource>()
    singleOf(::RoomLocalUserPreferenceDataSource).bind<LocalPreferencesDataSource>()
    singleOf(::RoomTransactionDataSource).bind<LocalTransactionDataSource>()
}