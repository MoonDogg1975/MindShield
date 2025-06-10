package com.mindshield.app.data.source

import com.mindshield.app.data.model.AuthRequest
import com.mindshield.app.data.model.AuthResponse
import com.mindshield.app.utils.Result

/**
 * Interface for authentication data source operations
 */
interface AuthDataSource {
    /**
     * Login with email and password
     */
    suspend fun login(email: String, password: String): Result<AuthResponse>
    
    /**
     * Register a new user
     */
    suspend fun register(email: String, password: String, name: String): Result<AuthResponse>
    
    /**
     * Refresh authentication token
     */
    suspend fun refreshToken(refreshToken: String): Result<AuthResponse>
    
    /**
     * Logout the current user
     */
    suspend fun logout()
    
    /**
     * Check if user is logged in
     */
    fun isLoggedIn(): Boolean
    
    /**
     * Get the current access token
     */
    fun getAccessToken(): String?
}
