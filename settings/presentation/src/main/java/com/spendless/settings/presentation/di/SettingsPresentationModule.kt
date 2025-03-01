package com.spendless.settings.presentation.di

import com.spendless.settings.presentation.home.SettingsHomeViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val settingsPresentationModule = module {
    viewModelOf(::SettingsHomeViewModel)
}