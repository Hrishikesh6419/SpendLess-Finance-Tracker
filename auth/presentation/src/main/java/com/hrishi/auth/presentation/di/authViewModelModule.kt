package com.hrishi.auth.presentation.di

import com.hrishi.auth.presentation.login.LoginViewModel
import com.hrishi.auth.presentation.register.RegisterViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val authViewModelModule = module {
    viewModelOf(::LoginViewModel)
    viewModelOf(::RegisterViewModel)
}