package com.mindshield.app.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mindshield.app.data.repository.AuthRepository
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
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _registerState = MutableStateFlow<RegisterState>(RegisterState.Initial)
    val registerState: StateFlow<RegisterState> = _registerState

    /**
     * Attempt to register a new user with the provided credentials
     */
    fun register(name: String, email: String, password: String, confirmPassword: String) {
        if (name.isBlank() || email.isBlank() || password.isBlank() || confirmPassword.isBlank()) {
            _registerState.value = RegisterState.Error("All fields are required")
            return
        }

        if (password != confirmPassword) {
            _registerState.value = RegisterState.Error("Passwords do not match")
            return
        }

        if (password.length < 6) {
            _registerState.value = RegisterState.Error("Password must be at least 6 characters")
            return
        }

        viewModelScope.launch {
            _registerState.value = RegisterState.Loading
            
            when (val result = authRepository.register(email, password, name)) {
                is Result.Success -> {
                    _registerState.value = RegisterState.Success
                }
                is Result.Error -> {
                    _registerState.value = RegisterState.Error(
                        result.message ?: "Registration failed. Please try again."
                    )
                }
                else -> {
                    _registerState.value = RegisterState.Error("An unexpected error occurred")
                }
            }
        }
    }

    /**
     * Reset the registration state to initial
     */
    fun resetState() {
        _registerState.value = RegisterState.Initial
    }
}

/**
 * Represents the different states of the registration process
 */
sealed class RegisterState {
    object Initial : RegisterState()
    object Loading : RegisterState()
    object Success : RegisterState()
    data class Error(val message: String) : RegisterState()
}
