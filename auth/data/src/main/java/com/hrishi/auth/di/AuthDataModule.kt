package com.hrishi.auth.di

import com.hrishi.domain.usecase.AuthUseCases
import com.hrishi.domain.usecase.IsUsernameDuplicateUseCase
import com.hrishi.domain.usecase.IsUsernameValidUseCase
import org.koin.dsl.module

val authDataModule = module {
    single { IsUsernameValidUseCase() }
    factory { IsUsernameDuplicateUseCase() }
    single { AuthUseCases(get(), get()) }
}