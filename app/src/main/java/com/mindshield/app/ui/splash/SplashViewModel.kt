package com.mindshield.app.ui.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mindshield.app.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for the Splash Screen
 */
@HiltViewModel
class SplashViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    /**
     * Check if the user is authenticated and navigate accordingly
     */
    fun checkAuthStatus(
        onAuthenticated: () -> Unit,
        onUnauthenticated: () -> Unit
    ) {
        viewModelScope.launch {
            // Optional: Add a small delay to show the splash screen
            delay(1500) // 1.5 seconds
            
            if (authRepository.isLoggedIn()) {
                onAuthenticated()
            } else {
                onUnauthenticated()
            }
            
            _isLoading.value = false
        }
    }
}
