package com.mindshield.app.auth

import com.mindshield.app.data.model.User
import com.mindshield.app.data.repository.AuthRepository
import com.mindshield.app.utils.Result
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Manages the authentication state of the application using StateFlows.
 * This provides a reactive way to observe authentication state changes.
 */
@Singleton
class AuthStateManager @Inject constructor(
    private val authRepository: AuthRepository,
    private val coroutineScope: CoroutineScope
) {
    private val _authState = MutableStateFlow<AuthState>(AuthState.Loading)
    
    /**
     * The current authentication state as a StateFlow that can be collected by the UI.
     */
    val authState: StateFlow<AuthState> = _authState

    init {
        // Start observing authentication state
        observeAuthState()
    }

    /**
     * Observes the authentication state from the repository and updates the UI state accordingly.
     */
    private fun observeAuthState() {
        coroutineScope.launch {
            authRepository.observeAuthState().collect { isAuthenticated ->
                updateAuthState(isAuthenticated)
            }
        }
    }
    
    /**
     * Updates the authentication state based on the provided authentication status.
     */
    private suspend fun updateAuthState(isAuthenticated: Boolean) {
        if (isAuthenticated) {
            // If authenticated, fetch the current user
            when (val result = authRepository.getCurrentUser()) {
                is Result.Success -> {
                    result.data?.let { user ->
                        _authState.value = AuthState.Authenticated(user)
                    } ?: run {
                        _authState.value = AuthState.Error("User data not found")
                    }
                }
                is Result.Error -> {
                    _authState.value = AuthState.Error(
                        result.exception.message ?: "Failed to fetch user data"
                    )
                }
            }
        } else {
            _authState.value = AuthState.Unauthenticated
        }
    }

    /**
     * Refreshes the authentication state by forcing a check against the repository.
     */
    fun refresh() {
        coroutineScope.launch {
            _authState.value = AuthState.Loading
            val isAuthenticated = authRepository.isLoggedIn()
            updateAuthState(isAuthenticated)
        }
    }
    
    /**
     * Clears the current authentication state (e.g., on logout).
     */
    fun clearAuthState() {
        _authState.value = AuthState.Unauthenticated
    }
    
    /**
     * Updates the authentication state with an error message.
     */
    fun setError(message: String) {
        _authState.value = AuthState.Error(message)
    }
}
