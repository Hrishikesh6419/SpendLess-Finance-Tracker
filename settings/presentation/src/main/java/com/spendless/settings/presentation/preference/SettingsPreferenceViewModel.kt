package com.spendless.settings.presentation.preference

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hrishi.core.domain.formatting.NumberFormatter
import com.hrishi.core.domain.model.PinAttempts
import com.hrishi.core.domain.preference.model.UserPreferences
import com.hrishi.core.domain.preference.usecase.PreferenceUseCase
import com.hrishi.core.domain.utils.Result
import com.spendless.session_management.domain.usecases.SessionUseCases
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SettingsPreferenceViewModel(
    private val sessionUseCases: SessionUseCases,
    private val preferenceUseCase: PreferenceUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsPreferencesViewState())
    val uiState = _uiState.asStateFlow()

    private val eventChannel = Channel<SettingsPreferencesEvent>()
    val events = eventChannel.receiveAsFlow()

    private var userPreferences: UserPreferences? = null

    init {
        fetchUserPreferences()
    }

    private fun fetchUserPreferences() {
        sessionUseCases.getSessionDataUseCase()
            .flatMapLatest { sessionData ->
                preferenceUseCase.getPreferencesUseCase(sessionData.userId)
            }
            .onEach { result ->
                if (result is Result.Success) {
                    userPreferences = result.data
                    updateUiState {
                        it.copy(
                            userId = result.data.userId,
                            expenseFormat = result.data.expenseFormat,
                            currency = result.data.currency,
                            decimalSeparator = result.data.decimalSeparator,
                            thousandsSeparator = result.data.thousandsSeparator,
                        )
                    }
                }
            }
            .launchIn(viewModelScope)
    }

    fun onAction(action: SettingsPreferencesAction) {
        when (action) {
            SettingsPreferencesAction.OnBackClicked -> emitEvent(SettingsPreferencesEvent.NavigateBack)

            is SettingsPreferencesAction.OnExpenseFormatUpdate -> updateUiState {
                it.copy(expenseFormat = action.format)
            }

            is SettingsPreferencesAction.OnDecimalSeparatorUpdate -> updateUiState {
                it.copy(decimalSeparator = action.format)
            }

            is SettingsPreferencesAction.OnThousandsSeparatorUpdate -> updateUiState {
                it.copy(thousandsSeparator = action.format)
            }

            is SettingsPreferencesAction.OnCurrencyUpdate -> updateUiState {
                it.copy(currency = action.currency)
            }

            SettingsPreferencesAction.OnSaveClicked -> handleOnSaveClicked()
        }
    }

    private fun handleOnSaveClicked() {
        viewModelScope.launch {
            userPreferences?.let { existingUserPreference ->
                val updatedPreferences = UserPreferences(
                    userId = _uiState.value.userId,
                    expenseFormat = _uiState.value.expenseFormat,
                    currency = _uiState.value.currency,
                    decimalSeparator = _uiState.value.decimalSeparator,
                    thousandsSeparator = _uiState.value.thousandsSeparator,
                    isBiometricEnabled = existingUserPreference.isBiometricEnabled,
                    sessionDuration = existingUserPreference.sessionDuration,
                    lockOutDuration = existingUserPreference.lockOutDuration,
                    allowedPinAttempts = PinAttempts.THREE
                )

                when (preferenceUseCase.setPreferencesUseCase(updatedPreferences)) {
                    is Result.Success -> emitEvent(SettingsPreferencesEvent.PreferencesSaved)
                    is Result.Error -> Unit
                }
            }
        }
    }

    private fun updateUiState(updateBlock: (SettingsPreferencesViewState) -> SettingsPreferencesViewState) {
        _uiState.update { currentState ->
            val newState = updateBlock(currentState)
            val isValidFormat = preferenceUseCase.isValidPreferenceUseCase(
                decimalSeparator = newState.decimalSeparator,
                thousandsSeparator = newState.thousandsSeparator
            )

            newState.copy(
                enableSaveButton = isValidFormat,
                exampleFormat = if (isValidFormat) formatExample(newState) else currentState.exampleFormat
            )
        }
    }

    private fun formatExample(state: SettingsPreferencesViewState): String {
        return NumberFormatter.formatAmount(
            amount = state.amount,
            expenseFormat = state.expenseFormat,
            decimalSeparator = state.decimalSeparator,
            thousandsSeparator = state.thousandsSeparator,
            currency = state.currency
        )
    }

    private fun emitEvent(event: SettingsPreferencesEvent) {
        viewModelScope.launch { eventChannel.send(event) }
    }
}