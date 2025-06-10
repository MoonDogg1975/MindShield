package com.mindshield.app.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mindshield.app.auth.AuthenticationManager
import com.mindshield.app.auth.AuthResult
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
    private val authManager: AuthenticationManager
) : ViewModel() {

    private val _loginState = MutableStateFlow<LoginUiState>(LoginUiState.Initial)
    val loginState: StateFlow<LoginUiState> = _loginState

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _loginState.value = LoginUiState.Loading
            
            when (val result = authManager.login(email, password)) {
                is Result.Success -> {
                    _loginState.value = LoginUiState.Success
                }
                is Result.Error -> {
                    _loginState.value = LoginUiState.Error(
                        message = result.exception.message ?: "Login failed"
                    )
                }
                else -> {
                    _loginState.value = LoginUiState.Error(
                        message = "Unexpected error occurred"
                    )
                }
            }
        }
    }
    
    fun resetState() {
        _loginState.value = LoginUiState.Initial
    }
}

/**
 * UI State for the Login screen
 */
sealed class LoginUiState {
    object Initial : LoginUiState()
    object Loading : LoginUiState()
    object Success : LoginUiState()
    data class Error(val message: String) : LoginUiState()
}
