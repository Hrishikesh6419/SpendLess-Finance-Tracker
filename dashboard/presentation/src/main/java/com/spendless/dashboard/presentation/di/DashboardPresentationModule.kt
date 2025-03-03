package com.spendless.dashboard.presentation.di

import com.spendless.dashboard.presentation.all_transactions.AllTransactionsViewModel
import com.spendless.dashboard.presentation.create_screen.CreateTransactionViewModel
import com.spendless.dashboard.presentation.dashboard.DashboardViewModel
import com.spendless.dashboard.presentation.export.ExportTransactionsViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

@OptIn(ExperimentalCoroutinesApi::class)
val dashboardPresentationModule = module {
    viewModelOf(::DashboardViewModel)
    viewModelOf(::CreateTransactionViewModel)
    viewModelOf(::AllTransactionsViewModel)
    viewModelOf(::ExportTransactionsViewModel)
}