package com.spendless.dashboard.data.di

import com.spendless.dashboard.domain.usecases.create_transactions.BuildTransactionUseCase
import com.spendless.dashboard.domain.usecases.create_transactions.CreateTransactionsUseCases
import com.spendless.dashboard.domain.usecases.create_transactions.GetTransactionHintUseCase
import com.spendless.dashboard.domain.usecases.create_transactions.IsExpenseCategoryVisibleUseCase
import com.spendless.dashboard.domain.usecases.create_transactions.IsValidInputUseCase
import com.spendless.dashboard.domain.usecases.dashboard.GetAllTransactionsDataUseCase
import com.spendless.dashboard.domain.usecases.dashboard.GetDashboardDataUseCase
import org.koin.dsl.module

val dashboardModule = module {
    single { GetDashboardDataUseCase(get(), get(), get()) }
    single { GetAllTransactionsDataUseCase(get(), get(), get()) }

    factory { GetTransactionHintUseCase() }
    factory { IsExpenseCategoryVisibleUseCase() }
    factory { IsValidInputUseCase() }
    factory { BuildTransactionUseCase(get(), get()) }
    single { CreateTransactionsUseCases(get(), get(), get(), get()) }
}