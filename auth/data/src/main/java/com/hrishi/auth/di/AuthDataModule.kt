package com.hrishi.auth.di

import com.hrishi.domain.usecase.AuthUseCases
import com.hrishi.domain.usecase.IsUsernameValidUseCase
import org.koin.dsl.module

val authDataModule = module {
    single { IsUsernameValidUseCase() }
    single { AuthUseCases(get()) }
}