package com.hrishi.spendless

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import com.hrishi.auth.data.di.authDataModule
import com.hrishi.auth.presentation.di.authViewModelModule
import com.hrishi.core.data.di.coreDataModule
import com.hrishi.spendless.di.appModule
import com.spendless.core.database.di.databaseModule
import com.spendless.dashboard.data.di.dashboardModule
import com.spendless.dashboard.presentation.di.dashboardPresentationModule
import com.spendless.session_management.data.di.sessionModule
import com.spendless.session_management.presentation.di.sessionPresentationModule
import com.spendless.settings.presentation.di.settingsPresentationModule
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import timber.log.Timber

class SpendLessApp : Application() {

    val applicationScope = CoroutineScope(SupervisorJob())

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        startKoin {
            androidLogger()
            androidContext(this@SpendLessApp)
            modules(
                appModule,
                authViewModelModule,
                authDataModule,
                coreDataModule,
                dashboardModule,
                databaseModule,
                sessionModule,
                dashboardPresentationModule,
                sessionPresentationModule,
                settingsPresentationModule,
            )
        }
    }
}