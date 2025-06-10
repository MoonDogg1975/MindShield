package com.mindshield.app.auth

import android.util.Log
import com.mindshield.app.data.model.AuthResponse
import com.mindshield.app.data.repository.AuthRepository
import com.mindshield.app.utils.Result
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import java.util.concurrent.atomic.AtomicBoolean
import javax.inject.Inject
import javax.inject.Singleton

private const val TAG = "TokenManager"

/**
 * Manages authentication tokens and handles token refresh
 */
@Singleton
class TokenManager @Inject constructor(
    private val authRepository: AuthRepository,
    private val coroutineScope: CoroutineScope
) {
    private val refreshLock = Any()
    private val isRefreshing = AtomicBoolean(false)
    private val refreshListeners = mutableListOf<suspend (Result<Unit>) -> Unit>()
    
    private val _tokenState = MutableStateFlow<TokenState>(TokenState.Loading)
    val tokenState: StateFlow<TokenState> = _tokenState

    init {
        // Initialize the token state
        initialize()
    }
    
    private fun initialize() {
        coroutineScope.launch {
            try {
                val isLoggedIn = authRepository.isLoggedIn()
                _tokenState.value = if (isLoggedIn) {
                    TokenState.Authenticated
                } else {
                    TokenState.Unauthenticated
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error initializing token state", e)
                _tokenState.value = TokenState.Unauthenticated
            }
        }
    }

    /**
     * Get a valid access token, refreshing if necessary
     * @return The access token, or null if not authenticated
     */
    suspend fun getValidToken(): String? {
        return when (val state = _tokenState.value) {
            is TokenState.Authenticated -> {
                // Check if token needs refresh
                // In a real app, you would check the token expiration
                // For now, we'll just return the token
                authRepository.getCurrentUser()?.let { it.accessToken }
            }
            else -> null
        }
    }

    /**
     * Refresh the access token
     * @return Result indicating success or failure
     */
    suspend fun refreshToken(): Result<Unit> {
        // Only allow one refresh at a time
        if (!isRefreshing.compareAndSet(false, true)) {
            // If another refresh is in progress, wait for it to complete
            return suspendCancellableCoroutine { continuation ->
                val listener: suspend (Result<Unit>) -> Unit = { result ->
                    continuation.resumeWith(Result.success(result))
                }
                
                synchronized(refreshLock) {
                    refreshListeners.add(listener)
                }
                
                continuation.invokeOnCancellation {
                    synchronized(refreshLock) {
                        refreshListeners.remove(listener)
                    }
                }
            }
        }
        
        try {
            val result = authRepository.refreshToken()
            
            // Update state based on result
            _tokenState.value = if (result is Result.Success) {
                TokenState.Authenticated
            } else {
                TokenState.Unauthenticated
            }
            
            // Notify all listeners
            synchronized(refreshLock) {
                refreshListeners.forEach { listener ->
                    coroutineScope.launch {
                        listener(result)
                    }
                }
                refreshListeners.clear()
            }
            
            return result
        } catch (e: Exception) {
            Log.e(TAG, "Error refreshing token", e)
            _tokenState.value = TokenState.Unauthenticated
            return Result.Error(e)
        } finally {
            isRefreshing.set(false)
        }
    }
    
    /**
     * Update tokens from an authentication response
     */
    fun updateTokens(authResponse: AuthResponse) {
        coroutineScope.launch {
            authRepository.saveAuthResponse(authResponse)
            _tokenState.value = TokenState.Authenticated
        }
    }
    
    /**
     * Clear all tokens (on logout)
     */
    fun clearTokens() {
        coroutineScope.launch {
            authRepository.clearAuthData()
            _tokenState.value = TokenState.Unauthenticated
        }
    }
}

/**
 * Represents the current token state
 */
sealed class TokenState {
    object Loading : TokenState()
    object Authenticated : TokenState()
    object Unauthenticated : TokenState()
}
