package com.mindshield.app.ui.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mindshield.app.auth.AuthenticationManager
import com.mindshield.app.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for the Registration screen
 */
@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val authManager: AuthenticationManager
) : ViewModel() {

    private val _registerState = MutableStateFlow<RegisterUiState>(RegisterUiState.Initial)
    val registerState: StateFlow<RegisterUiState> = _registerState

    fun register(name: String, email: String, password: String) {
        viewModelScope.launch {
            _registerState.value = RegisterUiState.Loading
            
            when (val result = authManager.register(name, email, password)) {
                is Result.Success -> {
                    _registerState.value = RegisterUiState.Success
                }
                is Result.Error -> {
                    _registerState.value = RegisterUiState.Error(
                        message = result.exception.message ?: "Registration failed"
                    )
                }
                else -> {
                    _registerState.value = RegisterUiState.Error(
                        message = "Unexpected error occurred"
                    )
                }
            }
        }
    }
    
    fun resetState() {
        _registerState.value = RegisterUiState.Initial
    }
}

/**
 * UI State for the Registration screen
 */
sealed class RegisterUiState {
    object Initial : RegisterUiState()
    object Loading : RegisterUiState()
    object Success : RegisterUiState()
    data class Error(val message: String) : RegisterUiState()
}
