package com.hrishi.core.domain.preference.data_source

import com.hrishi.core.domain.preference.model.UserPreferences
import com.hrishi.core.domain.utils.DataError
import com.hrishi.core.domain.utils.Result
import kotlinx.coroutines.flow.Flow

interface LocalPreferencesDataSource {
    suspend fun insertPreference(preferences: UserPreferences): Result<Unit, DataError>

    fun getPreferences(userId: Long): Flow<Result<UserPreferences, DataError>>
}