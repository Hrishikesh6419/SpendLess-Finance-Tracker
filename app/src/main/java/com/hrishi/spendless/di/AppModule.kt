package com.hrishi.spendless.di

import com.hrishi.spendless.MainViewModel
import com.hrishi.spendless.SpendLessApp
import kotlinx.coroutines.CoroutineScope
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {
    single<CoroutineScope> {
        (androidApplication() as SpendLessApp).applicationScope
    }

    viewModelOf(::MainViewModel)
}