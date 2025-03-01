package com.spendless.core.database.preferences.data_source

import com.hrishi.core.domain.preference.data_source.LocalPreferencesDataSource
import com.hrishi.core.domain.preference.model.UserPreferences
import com.hrishi.core.domain.utils.DataError
import com.hrishi.core.domain.utils.Result
import com.spendless.core.database.preferences.dao.UserPreferenceDao
import com.spendless.core.database.preferences.utils.toUserPreferenceEntity
import com.spendless.core.database.preferences.utils.toUserPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

class RoomLocalUserPreferenceDataSource(
    private val userPreferenceDao: UserPreferenceDao
) : LocalPreferencesDataSource {

    override suspend fun insertPreference(preferences: UserPreferences): Result<Unit, DataError> {
        return try {
            val result = userPreferenceDao.upsertUserPreference(preferences.toUserPreferenceEntity())
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(DataError.Local.UNKNOWN_DATABASE_ERROR)
        }
    }

    override fun getPreferences(userId: Long): Flow<Result<UserPreferences, DataError>> {
        return userPreferenceDao.getUserPreference(userId)
            .map { preferenceEntity ->
                preferenceEntity?.let {
                    Result.Success(it.toUserPreferences())
                } ?: Result.Error(DataError.Local.PREFERENCE_FETCH_ERROR)
            }
            .catch {
                emit(Result.Error(DataError.Local.UNKNOWN_DATABASE_ERROR))
            }
    }
}