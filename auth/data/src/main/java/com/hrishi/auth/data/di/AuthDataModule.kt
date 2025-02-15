package com.hrishi.auth.data.di

import com.hrishi.auth.data.repository.UserInfoRepositoryImpl
import com.hrishi.auth.domain.repository.UserInfoRepository
import com.hrishi.auth.domain.usecase.DecryptPinUseCase
import com.hrishi.auth.domain.usecase.EncryptPinUseCase
import com.hrishi.auth.domain.usecase.EncryptionUseCases
import com.hrishi.auth.domain.usecase.FormatExampleUseCase
import com.hrishi.auth.domain.usecase.InitiateLoginUseCase
import com.hrishi.auth.domain.usecase.IsUserNameDuplicateUseCase
import com.hrishi.auth.domain.usecase.IsUsernameValidUseCase
import com.hrishi.auth.domain.usecase.LoginUseCases
import com.hrishi.auth.domain.usecase.OnboardingPreferenceUseCases
import com.hrishi.auth.domain.usecase.RegisterUseCases
import com.hrishi.auth.domain.usecase.RegisterUserUseCase
import com.hrishi.auth.domain.usecase.SetPreferencesUseCase
import com.hrishi.auth.domain.usecase.ValidateSelectedPreferences
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val authDataModule = module {
    // Login
    single { IsUsernameValidUseCase() }
    factory { InitiateLoginUseCase(get(), get()) }
    single { LoginUseCases(get(), get()) }

    // Onboarding Preferences
    factory { ValidateSelectedPreferences() }
    factory { FormatExampleUseCase(get()) }
    factory { SetPreferencesUseCase(get()) }
    single { OnboardingPreferenceUseCases(get(), get(), get()) }

    // Encryption
    factory { EncryptPinUseCase(get()) }
    factory { DecryptPinUseCase(get()) }
    single { EncryptionUseCases(get(), get()) }

    // Register User
    factory { RegisterUserUseCase(get()) }
    factory { IsUserNameDuplicateUseCase(get()) }
    single { RegisterUseCases(get(), get()) }
    singleOf(::UserInfoRepositoryImpl).bind<UserInfoRepository>()
}