package com.hrishi.core.domain.model

enum class BiometricPromptStatus {
    ENABLE,
    DISABLE;

    fun getBooleanValue(): Boolean {
        return when (this) {
            ENABLE -> true
            DISABLE -> false
        }
    }

    fun getDisplayValue(): String {
        return when (this) {
            ENABLE -> "Enable"
            DISABLE -> "Disable"
        }
    }


    companion object {
        fun convertToBiometricPromptStatus(isEnabled: Boolean): BiometricPromptStatus {
            return if (isEnabled) {
                ENABLE
            } else {
                DISABLE
            }
        }
    }
}