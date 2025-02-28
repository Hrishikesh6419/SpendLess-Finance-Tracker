package com.spendless.dashboard.presentation.di

import com.spendless.dashboard.presentation.create_screen.CreateTransactionViewModel
import com.spendless.dashboard.presentation.dashboard.DashboardViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val dashboardPresentationModule = module {
    viewModelOf(::DashboardViewModel)
    viewModelOf(::CreateTransactionViewModel)
}