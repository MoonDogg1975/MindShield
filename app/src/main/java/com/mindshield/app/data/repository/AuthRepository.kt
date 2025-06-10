package com.mindshield.app.data.repository

import com.mindshield.app.data.model.AuthResponse
import com.mindshield.app.data.model.User
import com.mindshield.app.utils.Result
import kotlinx.coroutines.flow.Flow

/**
 * Interface defining the contract for authentication operations
 */
interface AuthRepository {
    /**
     * Login with email and password
     * @param email The user's email
     * @param password The user's password
     * @return Result indicating success or failure
     */
    suspend fun login(email: String, password: String): Result<Unit>
    
    /**
     * Register a new user
     * @param name The user's name
     * @param email The user's email
     * @param password The user's password
     * @return Result indicating success or failure
     */
    suspend fun register(name: String, email: String, password: String): Result<Unit>
    
    /**
     * Logout the current user
     * @return Result indicating success or failure
     */
    suspend fun logout(): Result<Unit>
    
    /**
     * Refresh the authentication token
     * @return Result indicating success or failure
     */
    suspend fun refreshToken(): Result<Unit>
    
    /**
     * Check if the user is logged in
     * @return true if the user is logged in, false otherwise
     */
    suspend fun isLoggedIn(): Boolean
    
    /**
     * Observe authentication state changes
     * @return A Flow that emits true when the user is logged in, false otherwise
     */
    fun observeAuthState(): Flow<Boolean>
    
    /**
     * Observe the current user
     * @return A Flow that emits the current user or null if not logged in
     */
    fun observeCurrentUser(): Flow<User?>
    
    /**
     * Get the current user
     * @return The current user or null if not logged in
     */
    suspend fun getCurrentUser(): User?
    
    /**
     * Save the FCM token
     * @param token The FCM token to save
     */
    suspend fun saveFcmToken(token: String)
    
    /**
     * Request a password reset email
     * @param email The user's email address
     * @return Result indicating success or failure
     */
    suspend fun forgotPassword(email: String): Result<Unit>
    
    /**
     * Reset password with a token
     * @param token The reset token from email
     * @param newPassword The new password
     * @return Result indicating success or failure
     */
    suspend fun resetPassword(token: String, newPassword: String): Result<Unit>
}
