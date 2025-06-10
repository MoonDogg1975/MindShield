package com.mindshield.app.auth

/**
 * Constants related to authentication
 */
object AuthConstants {
    // Shared Preferences keys
    const val PREF_IS_LOGGED_IN = "is_logged_in"
    const val PREF_ACCESS_TOKEN = "access_token"
    const val PREF_REFRESH_TOKEN = "refresh_token"
    const val PREF_USER_ID = "user_id"
    const val PREF_USER_EMAIL = "user_email"
    const val PREF_USER_NAME = "user_name"
    const val PREF_FCM_TOKEN = "fcm_token"
    const val PREF_LAST_LOGIN_TIMESTAMP = "last_login_timestamp"
    
    // Authentication headers
    const val HEADER_AUTHORIZATION = "Authorization"
    const val HEADER_BEARER = "Bearer"
    
    // Token expiration buffer (in milliseconds) - refresh token before it expires
    const val TOKEN_REFRESH_BUFFER_MS = 5 * 60 * 1000 // 5 minutes
    
    // Validation constants
    const val MIN_PASSWORD_LENGTH = 8
    const val MAX_PASSWORD_LENGTH = 72 // Bcrypt max length
    
    // Request codes
    const val RC_SIGN_IN = 9001
    
    // Error messages
    const val ERROR_INVALID_CREDENTIALS = "Invalid email or password"
    const val ERROR_NETWORK = "Network error. Please check your connection."
    const val ERROR_SERVER = "Server error. Please try again later."
    const val ERROR_UNKNOWN = "An unknown error occurred"
    const val ERROR_EMAIL_TAKEN = "Email is already in use"
    const val ERROR_WEAK_PASSWORD = "Password is too weak"
    const val ERROR_INVALID_EMAIL = "Invalid email address"
    const val ERROR_USER_DISABLED = "This account has been disabled"
    const val ERROR_USER_NOT_FOUND = "No account found with this email"
    const val ERROR_TOO_MANY_REQUESTS = "Too many requests. Please try again later."
    const val ERROR_OPERATION_NOT_ALLOWED = "This operation is not allowed"
    
    // Success messages
    const val MSG_LOGIN_SUCCESS = "Login successful"
    const val MSG_LOGOUT_SUCCESS = "Logged out successfully"
    const val MSG_REGISTRATION_SUCCESS = "Registration successful"
    const val MSG_PASSWORD_RESET_EMAIL_SENT = "Password reset email sent"
    const val MSG_PASSWORD_RESET_SUCCESS = "Password reset successful"
    const val MSG_VERIFICATION_EMAIL_SENT = "Verification email sent"
    
    // API endpoints (relative to base URL)
    object Endpoints {
        const val LOGIN = "auth/login"
        const val REGISTER = "auth/register"
        const val LOGOUT = "auth/logout"
        const val REFRESH_TOKEN = "auth/refresh"
        const val FORGOT_PASSWORD = "auth/forgot-password"
        const val RESET_PASSWORD = "auth/reset-password"
        const val USER_PROFILE = "auth/me"
        const val UPDATE_FCM_TOKEN = "auth/fcm-token"
    }
    
    // Validation patterns
    object Patterns {
        // Simple email pattern
        val EMAIL = Regex("^[A-Za-z0-9+_.-]+@(.+)$")
        
        // Password must contain at least 8 characters, including uppercase, lowercase, and number
        val PASSWORD = Regex("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\S+$).{8,}$")
    }
}
