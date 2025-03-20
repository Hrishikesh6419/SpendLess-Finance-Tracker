package com.spendless.dashboard.data.di

import com.spendless.dashboard.domain.usecases.dashboard.GetDashboardDataUseCase
import org.koin.dsl.module

val dashboardModule = module {
    single { GetDashboardDataUseCase(get(), get(), get()) }
}