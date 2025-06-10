package com.mindshield.app.auth

import android.content.Context
import android.util.Patterns
import com.mindshield.app.R
import com.mindshield.app.data.model.User
import com.mindshield.app.utils.Result
import java.util.regex.Pattern
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Utility class for common authentication operations and validations
 */
@Singleton
class AuthUtils @Inject constructor(
    private val context: Context
) {
    
    /**
     * Validates an email address
     * @return Validation result with error message if invalid
     */
    fun validateEmail(email: String): ValidationResult {
        return when {
            email.isBlank() -> ValidationResult.Error(context.getString(R.string.error_email_required))
            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> 
                ValidationResult.Error(context.getString(R.string.error_invalid_email))
            else -> ValidationResult.Success
        }
    }
    
    /**
     * Validates a password
     * @return Validation result with error message if invalid
     */
    fun validatePassword(password: String): ValidationResult {
        return when {
            password.isBlank() -> 
                ValidationResult.Error(context.getString(R.string.error_password_required))
            password.length < AuthConstants.MIN_PASSWORD_LENGTH -> 
                ValidationResult.Error(
                    context.getString(
                        R.string.error_password_too_short, 
                        AuthConstants.MIN_PASSWORD_LENGTH
                    )
                )
            password.length > AuthConstants.MAX_PASSWORD_LENGTH ->
                ValidationResult.Error(
                    context.getString(
                        R.string.error_password_too_long, 
                        AuthConstants.MAX_PASSWORD_LENGTH
                    )
                )
            !AuthConstants.Patterns.PASSWORD.matches(password) ->
                ValidationResult.Error(context.getString(R.string.error_password_weak))
            else -> ValidationResult.Success
        }
    }
    
    /**
     * Validates a name
     * @return Validation result with error message if invalid
     */
    fun validateName(name: String): ValidationResult {
        return when {
            name.isBlank() -> 
                ValidationResult.Error(context.getString(R.string.error_name_required))
            name.length < 2 -> 
                ValidationResult.Error(context.getString(R.string.error_name_too_short))
            name.length > 50 -> 
                ValidationResult.Error(context.getString(R.string.error_name_too_long))
            else -> ValidationResult.Success
        }
    }
    
    /**
     * Validates password confirmation
     * @return Validation result with error message if invalid
     */
    fun validatePasswordConfirmation(
        password: String, 
        confirmPassword: String
    ): ValidationResult {
        return when {
            confirmPassword.isBlank() -> 
                ValidationResult.Error(context.getString(R.string.error_confirm_password_required))
            password != confirmPassword -> 
                ValidationResult.Error(context.getString(R.string.error_passwords_dont_match))
            else -> ValidationResult.Success
        }
    }
    
    /**
     * Validates a registration form
     * @return Combined validation result
     */
    fun validateRegistrationForm(
        name: String,
        email: String,
        password: String,
        confirmPassword: String
    ): ValidationResult {
        return listOf(
            validateName(name),
            validateEmail(email),
            validatePassword(password),
            validatePasswordConfirmation(password, confirmPassword)
        ).firstOrNull { it is ValidationResult.Error } ?: ValidationResult.Success
    }
    
    /**
     * Validates a login form
     * @return Combined validation result
     */
    fun validateLoginForm(
        email: String,
        password: String
    ): ValidationResult {
        return listOf(
            validateEmail(email),
            validatePassword(password)
        ).firstOrNull { it is ValidationResult.Error } ?: ValidationResult.Success
    }
    
    /**
     * Maps authentication errors to user-friendly messages
     */
    fun getAuthErrorMessage(error: Throwable?): String {
        if (error == null) return context.getString(R.string.error_unknown)
        
        return when (error.message?.lowercase()) {
            "invalid_credentials" -> context.getString(R.string.error_invalid_credentials)
            "email_already_exists" -> context.getString(R.string.error_email_taken)
            "user_not_found" -> context.getString(R.string.error_user_not_found)
            "network_error" -> context.getString(R.string.error_network_connection)
            "too_many_attempts" -> context.getString(R.string.error_too_many_attempts)
            "weak_password" -> context.getString(R.string.error_password_weak)
            "invalid_email" -> context.getString(R.string.error_invalid_email)
            "user_disabled" -> context.getString(R.string.error_account_disabled)
            else -> error.message ?: context.getString(R.string.error_unknown)
        }
    }
    
    /**
     * Represents the result of a validation
     */
    sealed class ValidationResult {
        object Success : ValidationResult()
        data class Error(val message: String) : ValidationResult()
    }
}
