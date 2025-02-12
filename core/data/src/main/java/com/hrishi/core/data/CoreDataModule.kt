package com.hrishi.core.data

import com.hrishi.domain.formatting.NumberFormatter
import org.koin.dsl.module

val coreDataModule = module {
    single { NumberFormatter }
}