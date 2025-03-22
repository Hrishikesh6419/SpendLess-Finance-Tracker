package com.hrishi.spendless.di

import com.hrishi.presentation.ui.NavigationRequestHandler
import com.hrishi.spendless.MainViewModel
import com.hrishi.spendless.SpendLessApp
import kotlinx.coroutines.CoroutineScope
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single<CoroutineScope> {
        (androidApplication() as SpendLessApp).applicationScope
    }

    viewModel { MainViewModel(get(), get(), get(), get()) }
    factory<NavigationRequestHandler> { get<MainViewModel>() }
}