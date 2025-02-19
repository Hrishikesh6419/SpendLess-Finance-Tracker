package com.spendless.session_management.domain.repository

import com.spendless.session_management.domain.model.SessionData
import kotlinx.coroutines.flow.Flow

interface SessionRepository {

    suspend fun saveSession(sessionData: SessionData)

    suspend fun clearSession()

    suspend fun setSessionToExpired()

    fun getSessionData(): Flow<SessionData>

    fun isSessionExpired(): Flow<Boolean>

    suspend fun resetSessionExpiry()
}