package com.hrishi.auth.presentation.user_preference

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hrishi.auth.domain.usecase.EncryptionUseCases
import com.hrishi.auth.domain.usecase.OnboardingPreferenceUseCases
import com.hrishi.auth.domain.usecase.RegisterUseCases
import com.hrishi.auth.presentation.navigation.model.PreferencesScreenData
import com.hrishi.core.domain.auth.model.UserInfo
import com.hrishi.core.domain.model.LockoutDuration
import com.hrishi.core.domain.model.PinAttempts
import com.hrishi.core.domain.model.SessionDuration
import com.hrishi.core.domain.preference.model.UserPreferences
import com.hrishi.core.domain.preference.usecase.SettingsPreferenceUseCase
import com.hrishi.core.domain.utils.DataError
import com.hrishi.core.domain.utils.Result
import com.hrishi.presentation.ui.getRouteData
import com.spendless.session_management.domain.model.SessionData
import com.spendless.session_management.domain.usecases.SessionUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class OnboardingPreferencesViewModel(
    savedStateHandle: SavedStateHandle,
    private val onboardingPreferenceUseCases: OnboardingPreferenceUseCases,
    private val registerUseCases: RegisterUseCases,
    private val encryptionUseCases: EncryptionUseCases,
    private val sessionUseCase: SessionUseCase,
    private val settingsPreferenceUseCase: SettingsPreferenceUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(OnboardingPreferencesViewState())
    val uiState = _uiState.asStateFlow()

    private val eventChannel = Channel<OnboardingPreferencesEvent>()
    val events = eventChannel.receiveAsFlow()

    private val screenData = savedStateHandle.getRouteData<PreferencesScreenData>("screenData")

    fun onAction(action: OnboardingPreferencesAction) {
        viewModelScope.launch {
            when (action) {
                OnboardingPreferencesAction.OnBackClicked -> {
                    eventChannel.send(OnboardingPreferencesEvent.NavigateToRegisterScreen)
                }

                is OnboardingPreferencesAction.OnExpenseFormatUpdate -> {
                    updateUiState { it.copy(expenseFormat = action.format) }
                }

                is OnboardingPreferencesAction.OnDecimalSeparatorUpdate -> {
                    updateUiState { it.copy(decimalSeparator = action.format) }
                }

                is OnboardingPreferencesAction.OnThousandsSeparatorUpdate -> {
                    updateUiState { it.copy(thousandsSeparator = action.format) }
                }

                is OnboardingPreferencesAction.OnCurrencyUpdate -> {
                    updateUiState { it.copy(currency = action.currency) }
                }

                OnboardingPreferencesAction.OnStartClicked -> {
                    handleOnStartClicked()
                }
            }
        }
    }

    private suspend fun handleOnStartClicked() {
        val (encryptedPin, iv) = encryptionUseCases.encryptPinUseCase(screenData?.pin.orEmpty())

        val userInfo = UserInfo(
            username = screenData?.username.orEmpty(),
            encryptedPin = encryptedPin,
            iv = iv
        )

        val userIdResult = withContext(Dispatchers.IO) {
            registerUseCases.registerUserUseCase(userInfo)
        }

        var userId = -1L

        when (userIdResult) {
            is Result.Error -> {
                handleRegistrationError(userIdResult.error)
                return
            }
            is Result.Success -> {
                userId = userIdResult.data
            }
        }

        val userPreferences = UserPreferences(
            userId = userIdResult.data,
            expenseFormat = _uiState.value.expenseFormat,
            currency = _uiState.value.currency,
            decimalSeparator = _uiState.value.decimalSeparator,
            thousandsSeparator = _uiState.value.thousandsSeparator,
            isBiometricEnabled = false,
            sessionDuration = SessionDuration.ONE_MIN,
            lockOutDuration = LockoutDuration.FIFTEEN_SECONDS,
            allowedPinAttempts = PinAttempts.THREE
        )

        val preferencesResult = withContext(Dispatchers.IO) {
            settingsPreferenceUseCase.setPreferencesUseCase(userPreferences)
        }

        when (preferencesResult) {
            is Result.Error -> {
                handlePreferenceError(preferencesResult.error)
                return
            }
            is Result.Success -> Unit
        }
        val sessionData = SessionData(
            userId = userId,
            userName = screenData?.username.orEmpty(),
            sessionExpiryTime = 0
        )
        sessionUseCase.saveSessionUseCase(sessionData)
        eventChannel.send(OnboardingPreferencesEvent.NavigateToDashboardScreen)
    }

    private suspend fun handleRegistrationError(error: DataError) {
        val event = when (error) {
            DataError.Local.DUPLICATE_USER_ERROR -> OnboardingPreferencesEvent.Error.DuplicateEntry
            else -> OnboardingPreferencesEvent.Error.Generic
        }
        eventChannel.send(event)
    }

    private suspend fun handlePreferenceError(error: DataError) {
        val event = when (error) {
            DataError.Local.PREFERENCE_FETCH_ERROR -> OnboardingPreferencesEvent.Error.Generic
            else -> OnboardingPreferencesEvent.Error.Generic
        }
        eventChannel.send(event)
    }

    private fun updateUiState(updateBlock: (OnboardingPreferencesViewState) -> OnboardingPreferencesViewState) {
        _uiState.update { currentState ->
            val newState = updateBlock(currentState)
            val isValidFormat = onboardingPreferenceUseCases.validateSelectedPreferences(
                decimalSeparator = newState.decimalSeparator,
                thousandsSeparator = newState.thousandsSeparator
            )

            newState.copy(
                enableStartTracking = isValidFormat,
                exampleFormat = if (isValidFormat) formatExample(newState) else currentState.exampleFormat
            )
        }
    }

    private fun formatExample(state: OnboardingPreferencesViewState): String {
        return onboardingPreferenceUseCases.formatExampleUseCase(
            amount = state.amount,
            expenseFormat = state.expenseFormat,
            decimalSeparator = state.decimalSeparator,
            thousandsSeparator = state.thousandsSeparator,
            currency = state.currency
        )
    }
}
