package com.hrishi.core.data.di

import com.hrishi.core.data.repository.UserInfoRepositoryImpl
import com.hrishi.core.data.repository.UserPreferencesRepositoryImpl
import com.hrishi.core.data.security.AesEncryptionService
import com.hrishi.core.data.security.KeyManager
import com.hrishi.core.domain.auth.repository.UserInfoRepository
import com.hrishi.core.domain.auth.usecases.GetUserInfoUseCase
import com.hrishi.core.domain.auth.usecases.UserInfoUseCases
import com.hrishi.core.domain.formatting.NumberFormatter
import com.hrishi.core.domain.preference.repository.UserPreferencesRepository
import com.hrishi.core.domain.preference.usecase.GetPreferencesUseCase
import com.hrishi.core.domain.preference.usecase.SetPreferencesUseCase
import com.hrishi.core.domain.preference.usecase.SettingsPreferenceUseCase
import com.hrishi.core.domain.security.EncryptionService
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val coreDataModule = module {
    single { NumberFormatter }
    single { KeyManager.getOrCreateSecretKey() }
    single<EncryptionService> { AesEncryptionService(get()) }

    singleOf(::UserPreferencesRepositoryImpl).bind<UserPreferencesRepository>()

    factory { SetPreferencesUseCase(get()) }
    factory { GetPreferencesUseCase(get()) }
    single { SettingsPreferenceUseCase(get(), get()) }

    singleOf(::UserInfoRepositoryImpl).bind<UserInfoRepository>()

    factory { GetUserInfoUseCase(get(), get()) }
    single { UserInfoUseCases(get()) }
}