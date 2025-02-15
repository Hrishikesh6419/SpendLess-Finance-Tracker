package com.hrishi.core.data.repository

import com.hrishi.core.domain.preference.data_source.LocalPreferencesDataSource
import com.hrishi.core.domain.preference.model.UserPreferences
import com.hrishi.core.domain.preference.repository.UserPreferencesRepository
import com.hrishi.core.domain.utils.DataError
import com.hrishi.core.domain.utils.Result
import kotlinx.coroutines.flow.Flow

class UserPreferencesRepositoryImpl(
    private val localPreferencesDataSource: LocalPreferencesDataSource
) : UserPreferencesRepository {
    override suspend fun insertPreference(userPreferences: UserPreferences): Result<Unit, DataError> {
        return localPreferencesDataSource.insertPreference(userPreferences)
    }

    override fun getPreferences(userId: Long): Flow<Result<UserPreferences, DataError>> {
        return localPreferencesDataSource.getPreferences(userId)
    }
}