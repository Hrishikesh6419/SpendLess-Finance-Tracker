package com.hrishi.spendless

import android.app.Application
import com.hrishi.auth.data.di.authDataModule
import com.hrishi.auth.presentation.di.authViewModelModule
import com.hrishi.core.data.di.coreDataModule
import com.spendless.core.database.di.databaseModule
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import timber.log.Timber

class SpendLessApp : Application() {

    val applicationScope = CoroutineScope(SupervisorJob())

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        startKoin {
            androidLogger()
            androidContext(this@SpendLessApp)
            modules(
                authViewModelModule,
                authDataModule,
                coreDataModule,
                databaseModule,
            )
        }
    }
}