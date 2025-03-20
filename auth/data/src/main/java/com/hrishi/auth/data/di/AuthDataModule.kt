package com.hrishi.auth.data.di

import com.hrishi.auth.domain.usecase.AppendDigitUseCase
import com.hrishi.auth.domain.usecase.CreatePinUseCases
import com.hrishi.auth.domain.usecase.DeleteDigitUseCase
import com.hrishi.auth.domain.usecase.InitiateLoginUseCase
import com.hrishi.auth.domain.usecase.IsUserNameDuplicateUseCase
import com.hrishi.auth.domain.usecase.IsUsernameValidUseCase
import com.hrishi.auth.domain.usecase.LoginUseCases
import com.hrishi.auth.domain.usecase.OnboardingPreferenceUseCases
import com.hrishi.auth.domain.usecase.RegisterUseCases
import com.hrishi.auth.domain.usecase.RegisterUserUseCase
import com.hrishi.auth.domain.usecase.ValidatePinMatchUseCase
import com.hrishi.auth.domain.usecase.ValidateSelectedPreferences
import org.koin.dsl.module

val authDataModule = module {
    // Login
    single { IsUsernameValidUseCase() }
    factory { InitiateLoginUseCase(get()) }
    single { LoginUseCases(get(), get()) }

    // Onboarding Preferences
    factory { ValidateSelectedPreferences() }
    single { OnboardingPreferenceUseCases(get()) }

    // Register User
    factory { RegisterUserUseCase(get()) }
    factory { IsUserNameDuplicateUseCase(get()) }
    single { RegisterUseCases(get(), get()) }

    // Create Pin
    factory { AppendDigitUseCase() }
    factory { DeleteDigitUseCase() }
    factory { ValidatePinMatchUseCase() }
    single { CreatePinUseCases(get(), get(), get()) }
}