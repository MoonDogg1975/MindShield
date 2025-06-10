package com.mindshield.app.auth

import com.mindshield.app.data.model.User
import com.mindshield.app.data.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Centralized authentication manager that provides authentication state and user information
 * to the rest of the application.
 */
@Singleton
class AuthenticationManager @Inject constructor(
    private val authRepository: AuthRepository
) {
    /**
     * Flow that emits the current authentication state
     */
    val isAuthenticated: Flow<Boolean> = authRepository.observeAuthState()

    /**
     * Flow that emits the current user if authenticated, null otherwise
     */
    val currentUser: Flow<User?> = authRepository.observeCurrentUser()

    /**
     * Check if the user is currently logged in
     * @return true if the user is logged in, false otherwise
     */
    suspend fun isLoggedIn(): Boolean = authRepository.isLoggedIn()

    /**
     * Get the current access token
     * @return The current access token or null if not authenticated
     */
    suspend fun getAccessToken(): String? = authRepository.getCurrentUser()?.let { 
        // In a real implementation, you would get the token from the repository
        // For now, we'll return null as the token management is handled internally
        null
    }

    /**
     * Get the current user
     * @return The current user or null if not authenticated
     */
    suspend fun getCurrentUser(): User? = authRepository.getCurrentUser()

    /**
     * Login with email and password
     * @param email The user's email
     * @param password The user's password
     * @return Result indicating success or failure
     */
    suspend fun login(email: String, password: String) = authRepository.login(email, password)

    /**
     * Register a new user
     * @param name The user's name
     * @param email The user's email
     * @param password The user's password
     * @return Result indicating success or failure
     */
    suspend fun register(name: String, email: String, password: String) =
        authRepository.register(name, email, password)

    /**
     * Logout the current user
     * @return Result indicating success or failure
     */
    suspend fun logout() = authRepository.logout()

    /**
     * Refresh the authentication token
     * @return Result indicating success or failure
     */
    suspend fun refreshToken() = authRepository.refreshToken()

    /**
     * Request a password reset email
     * @param email The user's email address
     * @return Result indicating success or failure
     */
    suspend fun forgotPassword(email: String) = authRepository.forgotPassword(email)

    /**
     * Reset password with a token
     * @param token The reset token from email
     * @param newPassword The new password
     * @return Result indicating success or failure
     */
    suspend fun resetPassword(token: String, newPassword: String) =
        authRepository.resetPassword(token, newPassword)

    /**
     * Save the FCM token
     * @param token The FCM token to save
     */
    suspend fun saveFcmToken(token: String) = authRepository.saveFcmToken(token)
}
