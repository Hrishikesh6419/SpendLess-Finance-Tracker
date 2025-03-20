package com.hrishi.auth.presentation.di

import com.hrishi.auth.presentation.create_pin.CreatePinViewModel
import com.hrishi.auth.presentation.login.LoginViewModel
import com.hrishi.auth.presentation.register.RegisterViewModel
import com.hrishi.auth.presentation.user_preference.OnboardingPreferencesViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val authViewModelModule = module {
    viewModelOf(::LoginViewModel)
    viewModelOf(::RegisterViewModel)
    viewModelOf(::OnboardingPreferencesViewModel)
    viewModelOf(::CreatePinViewModel)
}