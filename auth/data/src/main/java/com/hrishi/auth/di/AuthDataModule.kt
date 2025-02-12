package com.hrishi.auth.di

import com.hrishi.domain.usecase.FormatExampleUseCase
import com.hrishi.domain.usecase.IsUsernameDuplicateUseCase
import com.hrishi.domain.usecase.IsUsernameValidUseCase
import com.hrishi.domain.usecase.LoginUseCases
import com.hrishi.domain.usecase.OnboardingPreferenceUseCases
import com.hrishi.domain.usecase.ValidateSelectedPreferences
import org.koin.dsl.module

val authDataModule = module {
    // Login
    single { IsUsernameValidUseCase() }
    factory { IsUsernameDuplicateUseCase() }
    single { LoginUseCases(get(), get()) }

    // Onboarding Preferences
    factory { ValidateSelectedPreferences() }
    factory { FormatExampleUseCase(get()) }
    single { OnboardingPreferenceUseCases(get(), get()) }
}