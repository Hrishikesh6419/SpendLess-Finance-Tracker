package com.hrishi.auth.di

import com.hrishi.domain.usecase.IsUsernameDuplicateUseCase
import com.hrishi.domain.usecase.IsUsernameValidUseCase
import com.hrishi.domain.usecase.LoginUseCases
import com.hrishi.domain.usecase.OnboardingPreferenceUseCases
import org.koin.dsl.module

val authDataModule = module {
    single { IsUsernameValidUseCase() }
    factory { IsUsernameDuplicateUseCase() }
    single { LoginUseCases(get(), get()) }
    single { OnboardingPreferenceUseCases(get(), get()) }
}