package com.spendless.session_management.domain.usecases

import com.spendless.session_management.domain.repository.SessionRepository
import kotlinx.coroutines.flow.Flow

data class SessionUseCase(
    val startSessionUseCase: StartSessionUseCase,
    val isSessionExpiredUseCase: GetSessionStatusUseCase,
    val clearSessionUseCase: ClearSessionUseCase,
    val checkSessionExpiryUseCase: CheckSessionExpiryUseCase,
)

class StartSessionUseCase(private val sessionRepository: SessionRepository) {
    suspend operator fun invoke() {
        sessionRepository.startSession()
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

class CheckSessionExpiryUseCase(private val sessionRepository: SessionRepository) {
    suspend operator fun invoke() {
        sessionRepository.checkAndUpdateSessionExpiry()
    }
}