package com.spendless.session_management.data.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import com.spendless.session_management.data.SessionPreferences
import com.spendless.session_management.data.repository.SessionRepositoryImpl
import com.spendless.session_management.data.utils.SessionSerializer
import com.spendless.session_management.domain.repository.SessionRepository
import com.spendless.session_management.domain.usecases.CheckSessionExpiryUseCase
import com.spendless.session_management.domain.usecases.ClearSessionUseCase
import com.spendless.session_management.domain.usecases.GetSessionDataUseCase
import com.spendless.session_management.domain.usecases.GetSessionStatusUseCase
import com.spendless.session_management.domain.usecases.ResetSessionExpiryUseCase
import com.spendless.session_management.domain.usecases.SaveSessionUseCase
import com.spendless.session_management.domain.usecases.SessionUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

private fun provideSessionDataStore(context: Context): DataStore<SessionPreferences> {
    return DataStoreFactory.create(
        serializer = SessionSerializer,
        produceFile = { context.dataStoreFile("session_prefs.pb") },
        corruptionHandler = null,
        scope = CoroutineScope(Dispatchers.IO)
    )
}

val sessionModule = module {
    single<DataStore<SessionPreferences>> { provideSessionDataStore(androidContext()) }

    factory { SaveSessionUseCase(get()) }
    factory { GetSessionStatusUseCase(get()) }
    factory { ClearSessionUseCase(get()) }
    factory { CheckSessionExpiryUseCase(get()) }
    factory { GetSessionDataUseCase(get()) }
    factory { ResetSessionExpiryUseCase(get()) }
    single { SessionUseCase(get(), get(), get(), get(), get(), get()) }

    singleOf(::SessionRepositoryImpl).bind<SessionRepository>()
}