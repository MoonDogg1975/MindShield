package com.mindshield.app.auth

import com.mindshield.app.data.model.User

/**
 * Represents the result of an authentication operation
 */
sealed class AuthResult<out T> {
    data class Success<T>(val data: T) : AuthResult<T>() {
        fun getOrNull(): T? = data
    }
    
    data class Error(val exception: Throwable) : AuthResult<Nothing>() {
        val message: String = exception.message ?: "An error occurred"
    }
    
    data class Invalid(val error: String) : AuthResult<Nothing>()
    
    object Loading : AuthResult<Nothing>()
    
    object Unauthorized : AuthResult<Nothing>()
    
    object NetworkError : AuthResult<Nothing>()
    
    object Empty : AuthResult<Nothing>()
}

/**
 * Represents the authentication state
 */
data class AuthState(
    val user: User? = null,
    val isAuthenticated: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null
) {
    companion object {
        val Initial = AuthState()
        val Loading = AuthState(isLoading = true)
        
        fun authenticated(user: User) = AuthState(
            user = user,
            isAuthenticated = true
        )
        
        fun error(error: String) = AuthState(error = error)
    }
}

/**
 * Represents the result of a login operation
 */
data class LoginResult(
    val success: Boolean,
    val user: User? = null,
    val error: String? = null,
    val requiresVerification: Boolean = false,
    val isNewUser: Boolean = false
) {
    companion object {
        fun success(user: User, isNewUser: Boolean = false) = 
            LoginResult(true, user = user, isNewUser = isNewUser)
            
        fun error(error: String) = LoginResult(false, error = error)
        
        fun requiresVerification() = LoginResult(
            success = false,
            requiresVerification = true
        )
    }
}

/**
 * Represents the result of a registration operation
 */
data class RegistrationResult(
    val success: Boolean,
    val user: User? = null,
    val error: String? = null,
    val requiresVerification: Boolean = false
) {
    companion object {
        fun success(user: User) = RegistrationResult(true, user = user)
        fun error(error: String) = RegistrationResult(false, error = error)
        fun requiresVerification() = RegistrationResult(
            success = true,
            requiresVerification = true
        )
    }
}

/**
 * Represents the result of a password reset operation
 */
data class PasswordResetResult(
    val success: Boolean,
    val message: String,
    val error: String? = null
) {
    companion object {
        fun success(message: String) = PasswordResetResult(true, message)
        fun error(message: String, error: String? = null) = 
            PasswordResetResult(false, message, error)
    }
}
