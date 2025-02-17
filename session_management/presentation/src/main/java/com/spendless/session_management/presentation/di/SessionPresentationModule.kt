package com.spendless.session_management.presentation.di

import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import com.spendless.session_management.presentation.pin_prompt.PinPromptViewModel

val sessionPresentationModule = module {
    viewModelOf(::PinPromptViewModel)
}