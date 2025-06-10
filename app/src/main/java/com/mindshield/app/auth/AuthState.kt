package com.mindshield.app.auth

import com.mindshield.app.data.model.User

/**
 * Represents the authentication state of the user
 */
sealed class AuthState {
    /**
     * Initial state, not determined yet
     */
    object Initial : AuthState()
    
    /**
     * User is authenticated
     */
    data class Authenticated(val user: User) : AuthState()
    
    /**
     * User is not authenticated
     */
    object Unauthenticated : AuthState()
    
    /**
     * Authentication is in progress
     */
    object Loading : AuthState()
    
    /**
     * Authentication failed with an error
     */
    data class Error(val message: String) : AuthState()
    
    /**
     * Check if the user is currently authenticated
     */
    val isAuthenticated: Boolean
        get() = this is Authenticated
    
    /**
     * Get the current user if authenticated, null otherwise
     */
    val currentUser: User?
        get() = (this as? Authenticated)?.user
}
