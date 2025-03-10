package com.hrishi.core.data.di

import com.hrishi.core.data.export.repository.ExportRepositoryImpl
import com.hrishi.core.data.repository.TransactionRepositoryImpl
import com.hrishi.core.data.repository.UserInfoRepositoryImpl
import com.hrishi.core.data.repository.UserPreferencesRepositoryImpl
import com.hrishi.core.data.security.AesEncryptionService
import com.hrishi.core.data.security.KeyManager
import com.hrishi.core.domain.auth.repository.UserInfoRepository
import com.hrishi.core.domain.auth.usecases.GetUserInfoUseCase
import com.hrishi.core.domain.auth.usecases.UserInfoUseCases
import com.hrishi.core.domain.export.repository.ExportRepository
import com.hrishi.core.domain.export.usecases.ExportTransactionUseCase
import com.hrishi.core.domain.export.usecases.ExportTransactionsUseCases
import com.hrishi.core.domain.formatting.NumberFormatter
import com.hrishi.core.domain.preference.repository.UserPreferencesRepository
import com.hrishi.core.domain.preference.usecase.GetPreferencesUseCase
import com.hrishi.core.domain.preference.usecase.SetPreferencesUseCase
import com.hrishi.core.domain.preference.usecase.SettingsPreferenceUseCase
import com.hrishi.core.domain.preference.usecase.ValidateSelectedPreferences
import com.hrishi.core.domain.security.EncryptionService
import com.hrishi.core.domain.transactions.repository.TransactionRepository
import com.hrishi.core.domain.transactions.usecases.GetAccountBalanceUseCase
import com.hrishi.core.domain.transactions.usecases.GetDueRecurringTransactionsUseCase
import com.hrishi.core.domain.transactions.usecases.GetLargestTransactionUseCase
import com.hrishi.core.domain.transactions.usecases.GetMostPopularExpenseCategoryUseCase
import com.hrishi.core.domain.transactions.usecases.GetNextRecurringDateUseCase
import com.hrishi.core.domain.transactions.usecases.GetPreviousWeekTotalUseCase
import com.hrishi.core.domain.transactions.usecases.GetRecurringTransactionSeriesUseCase
import com.hrishi.core.domain.transactions.usecases.GetTransactionsForUserUseCase
import com.hrishi.core.domain.transactions.usecases.GetTransactionsGroupedByDateUseCase
import com.hrishi.core.domain.transactions.usecases.InsertTransactionUseCase
import com.hrishi.core.domain.transactions.usecases.ProcessRecurringTransactionsUseCase
import com.hrishi.core.domain.transactions.usecases.TransactionUseCases
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val coreDataModule = module {
    single { NumberFormatter }
    single { KeyManager.getOrCreateSecretKey() }
    single<EncryptionService> { AesEncryptionService(get()) }

    singleOf(::UserPreferencesRepositoryImpl).bind<UserPreferencesRepository>()

    factory { SetPreferencesUseCase(get()) }
    factory { GetPreferencesUseCase(get()) }
    factory { ValidateSelectedPreferences() }
    single { SettingsPreferenceUseCase(get(), get(), get()) }

    singleOf(::UserInfoRepositoryImpl).bind<UserInfoRepository>()

    factory { GetUserInfoUseCase(get(), get()) }
    single { UserInfoUseCases(get()) }

    factory { InsertTransactionUseCase(get()) }
    factory { GetTransactionsForUserUseCase(get()) }
    factory { GetRecurringTransactionSeriesUseCase(get()) }
    factory { GetDueRecurringTransactionsUseCase(get()) }
    factory { GetAccountBalanceUseCase(get()) }
    factory { GetMostPopularExpenseCategoryUseCase(get()) }
    factory { GetLargestTransactionUseCase(get()) }
    factory { GetPreviousWeekTotalUseCase(get()) }
    factory { GetTransactionsGroupedByDateUseCase() }
    factory { GetNextRecurringDateUseCase() }
    factory { ProcessRecurringTransactionsUseCase(get(), get(), get()) }
    single {
        TransactionUseCases(
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get()
        )
    }
    singleOf(::TransactionRepositoryImpl).bind<TransactionRepository>()
    singleOf(::ExportRepositoryImpl).bind<ExportRepository>()

    factory { ExportTransactionUseCase(get(), get()) }
    single { ExportTransactionsUseCases(get()) }
}