package com.spendless.session_management.domain.usecases

import com.spendless.session_management.domain.model.SessionData
import com.spendless.session_management.domain.repository.SessionRepository
import kotlinx.coroutines.flow.Flow

data class SessionUseCase(
    val saveSessionUseCase: SaveSessionUseCase,
    val isSessionExpiredUseCase: GetSessionStatusUseCase,
    val setSessionExpiredUseCase: SetSessionExpiredUseCase,
    val clearSessionUseCase: ClearSessionUseCase,
    val getSessionDataUseCase: GetSessionDataUseCase,
    val resetSessionExpiryUseCase: ResetSessionExpiryUseCase
)

class SaveSessionUseCase(private val sessionRepository: SessionRepository) {
    suspend operator fun invoke(sessionData: SessionData) {
        sessionRepository.saveSession(sessionData)
    }
}

class GetSessionStatusUseCase(private val sessionRepository: SessionRepository) {
    operator fun invoke(): Flow<Boolean> {
        return sessionRepository.isSessionExpired()
    }
}

class ClearSessionUseCase(private val sessionRepository: SessionRepository) {
    suspend operator fun invoke() {
        sessionRepository.clearSession()
    }
}


class SetSessionExpiredUseCase(private val sessionRepository: SessionRepository) {
    suspend operator fun invoke() {
        sessionRepository.setSessionToExpired()
    }
}

class GetSessionDataUseCase(private val sessionRepository: SessionRepository) {
    operator fun invoke(): Flow<SessionData> {
        return sessionRepository.getSessionData()
    }
}

class ResetSessionExpiryUseCase(private val sessionRepository: SessionRepository) {
    suspend operator fun invoke() {
        sessionRepository.resetSessionExpiry()
    }
}