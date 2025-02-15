package com.spendless.core.database.di

import androidx.room.Room
import com.hrishi.core.domain.auth.data_source.LocalUserInfoDataSource
import com.hrishi.core.domain.preference.data_source.LocalPreferencesDataSource
import com.spendless.core.database.SpendLessDatabase
import com.spendless.core.database.auth.data_source.RoomLocalUserInfoDataSource
import com.spendless.core.database.preferences.data_source.RoomLocalUserPreferenceDataSource
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val databaseModule = module {
    single {
        Room.databaseBuilder(
            androidApplication(),
            SpendLessDatabase::class.java,
            "spendless.db"
        ).build()
    }
    single { get<SpendLessDatabase>().userInfoDao }
    single { get<SpendLessDatabase>().userPreferenceDao }

    singleOf(::RoomLocalUserInfoDataSource).bind<LocalUserInfoDataSource>()
    singleOf(::RoomLocalUserPreferenceDataSource).bind<LocalPreferencesDataSource>()
}