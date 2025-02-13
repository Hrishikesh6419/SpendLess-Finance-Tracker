package com.hrishi.core.data.di

import com.hrishi.core.data.security.AesEncryptionService
import com.hrishi.core.data.security.KeyManager
import com.hrishi.core.domain.formatting.NumberFormatter
import com.hrishi.core.domain.security.EncryptionService
import org.koin.dsl.module

val coreDataModule = module {
    single { NumberFormatter }
    single { KeyManager.getOrCreateSecretKey() }
    single<EncryptionService> { AesEncryptionService(get()) }
}