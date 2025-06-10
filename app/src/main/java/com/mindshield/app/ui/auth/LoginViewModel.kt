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
 * ViewModel for the Login screen
 */
@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _loginState = MutableStateFlow<LoginState>(LoginState.Initial)
    val loginState: StateFlow<LoginState> = _loginState

    /**
     * Attempt to log in with the provided credentials
     */
    fun login(email: String, password: String) {
        if (email.isBlank() || password.isBlank()) {
            _loginState.value = LoginState.Error("Email and password cannot be empty")
            return
        }

        viewModelScope.launch {
            _loginState.value = LoginState.Loading
            
            when (val result = authRepository.login(email, password)) {
                is Result.Success -> {
                    _loginState.value = LoginState.Success
                }
                is Result.Error -> {
                    _loginState.value = LoginState.Error(
                        result.message ?: "Login failed. Please try again."
                    )
                }
                else -> {
                    _loginState.value = LoginState.Error("An unexpected error occurred")
                }
            }
        }
    }

    /**
     * Reset the login state to initial
     */
    fun resetState() {
        _loginState.value = LoginState.Initial
    }
}

/**
 * Represents the different states of the login process
 */
sealed class LoginState {
    object Initial : LoginState()
    object Loading : LoginState()
    object Success : LoginState()
    data class Error(val message: String) : LoginState()
}
