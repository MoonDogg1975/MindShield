package com.mindshield.app.auth

import com.mindshield.app.R
import com.mindshield.app.utils.Result

/**
 * Extension functions for authentication-related validations
 */

/**
 * Validates an email address
 * @return Result.Success if valid, Result.Error with message if invalid
 */
fun String.validateEmail(): Result<Unit> {
    return when {
        isBlank() -> Result.Error(Exception("Email cannot be empty"))
        !AuthConstants.Patterns.EMAIL.matches(this) -> 
            Result.Error(Exception("Please enter a valid email address"))
        else -> Result.Success(Unit)
    }
}

/**
 * Validates a password
 * @return Result.Success if valid, Result.Error with message if invalid
 */
fun String.validatePassword(): Result<Unit> {
    return when {
        isBlank() -> Result.Error(Exception("Password cannot be empty"))
        length < AuthConstants.MIN_PASSWORD_LENGTH -> 
            Result.Error(Exception("Password must be at least ${AuthConstants.MIN_PASSWORD_LENGTH} characters"))
        length > AuthConstants.MAX_PASSWORD_LENGTH ->
            Result.Error(Exception("Password cannot exceed ${AuthConstants.MAX_PASSWORD_LENGTH} characters"))
        !AuthConstants.Patterns.PASSWORD.matches(this) ->
            Result.Error(Exception("Password must contain at least one uppercase letter, one lowercase letter, and one number"))
        else -> Result.Success(Unit)
    }
}

/**
 * Validates a name
 * @return Result.Success if valid, Result.Error with message if invalid
 */
fun String.validateName(): Result<Unit> {
    return when {
        isBlank() -> Result.Error(Exception("Name cannot be empty"))
        length < 2 -> Result.Error(Exception("Name is too short"))
        length > 50 -> Result.Error(Exception("Name is too long"))
        else -> Result.Success(Unit)
    }
}

/**
 * Validates password confirmation
 * @param password The original password
 * @return Result.Success if passwords match, Result.Error if they don't
 */
fun String.validatePasswordConfirmation(password: String): Result<Unit> {
    return if (this != password) {
        Result.Error(Exception("Passwords do not match"))
    } else {
        Result.Success(Unit)
    }
}

/**
 * Maps authentication errors to user-friendly messages
 * @return A user-friendly error message
 */
fun Throwable.toAuthMessage(): String {
    return when (val message = message?.lowercase() ?: "") {
        "invalid_credentials" -> "Invalid email or password"
        "email_already_exists" -> "This email is already registered"
        "invalid_token" -> "Session expired. Please log in again."
        "token_expired" -> "Session expired. Please log in again."
        "user_not_found" -> "No account found with this email"
        "network_error" -> "Network error. Please check your connection."
        "too_many_attempts" -> "Too many attempts. Please try again later."
        "weak_password" -> "Password is too weak. Please use a stronger password."
        "invalid_email" -> "Please enter a valid email address"
        else -> message.ifBlank { "An error occurred. Please try again." }
    }
}

/**
 * Extension function to get string resource ID for authentication error messages
 */
fun Throwable.toAuthMessageResId(): Int {
    return when (this) {
        is java.net.UnknownHostException -> R.string.error_network_connection
        is java.net.SocketTimeoutException -> R.string.error_network_timeout
        is java.net.ConnectException -> R.string.error_network_connection
        is java.net.SocketException -> R.string.error_network_connection
        is javax.net.ssl.SSLHandshakeException -> R.string.error_ssl_handshake
        is java.io.IOException -> R.string.error_io
        is com.google.gson.JsonSyntaxException -> R.string.error_invalid_response
        is com.google.gson.JsonParseException -> R.string.error_parsing_response
        else -> R.string.error_unknown
    }
}
