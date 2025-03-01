package com.hrishi.core.domain.utils

sealed interface DataError : Error {

    enum class Local : DataError {
        DISK_FULL,
        INVALID_CREDENTIALS,
        INSERT_USER_ERROR,
        DUPLICATE_USER_ERROR,
        USER_FETCH_ERROR,
        INSERT_PREFERENCE_ERROR,
        PREFERENCE_FETCH_ERROR,
        UNKNOWN_DATABASE_ERROR,
        TRANSACTION_FETCH_ERROR
    }
}