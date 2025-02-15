package com.hrishi.core.domain.preference.repository

import com.hrishi.core.domain.preference.model.UserPreferences
import com.hrishi.core.domain.utils.DataError
import com.hrishi.core.domain.utils.Result
import kotlinx.coroutines.flow.Flow

interface UserPreferencesRepository {
    suspend fun insertPreference(userPreferences: UserPreferences): Result<Unit, DataError>

    fun getPreferences(userId: Long): Flow<Result<UserPreferences, DataError>>
}