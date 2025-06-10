package com.mindshield.app.data.source.remote

import com.mindshield.app.data.model.AuthRequest
import com.mindshield.app.data.model.AuthResponse
import com.mindshield.app.data.model.User
import com.mindshield.app.data.source.AuthDataSource
import com.mindshield.app.utils.Result
import com.mindshield.app.utils.Result.Error
import com.mindshield.app.utils.Result.Success
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

/**
 * Interface defining the remote data source for authentication data
 */
interface AuthRemoteDataSource {
    /**
     * Login with email and password
     * @param email The user's email
     * @param password The user's password
     * @param fcmToken Optional FCM token for push notifications
     * @return Result containing AuthResponse on success or error on failure
     */
    suspend fun login(
        email: String, 
        password: String,
        fcmToken: String? = null
    ): Result<AuthResponse>
    
    /**
     * Register a new user
     * @param name The user's name
     * @param email The user's email
     * @param password The user's password
     * @param fcmToken Optional FCM token for push notifications
     * @return Result containing AuthResponse on success or error on failure
     */
    suspend fun register(
        name: String, 
        email: String, 
        password: String,
        fcmToken: String? = null
    ): Result<AuthResponse>
    
    /**
     * Refresh the authentication token
     * @param refreshToken The refresh token
     * @return Result containing new AuthResponse on success or error on failure
     */
    suspend fun refreshToken(refreshToken: String): Result<AuthResponse>
    
    /**
     * Logout the current user
     * @param accessToken The current access token
     * @return Result<Unit> indicating success or failure
     */
    suspend fun logout(accessToken: String): Result<Unit>
    
    /**
     * Get the current authenticated user's profile
     * @param accessToken The current access token
     * @return Result containing User on success or error on failure
     */
    suspend fun getCurrentUser(accessToken: String): Result<User>
    
    /**
     * Request a password reset email
     * @param email The user's email address
     * @return Result<Unit> indicating success or failure
     */
    suspend fun forgotPassword(email: String): Result<Unit>
    
    /**
     * Reset password with a token
     * @param token The reset token from email
     * @param newPassword The new password
     * @return Result<Unit> indicating success or failure
     */
    suspend fun resetPassword(token: String, newPassword: String): Result<Unit>
    
    /**
     * Update the FCM token on the server
     * @param accessToken The current access token
     * @param fcmToken The new FCM token
     * @return Result<Unit> indicating success or failure
     */
    suspend fun updateFcmToken(accessToken: String, fcmToken: String): Result<Unit>
}

/**
 * Remote data source implementation for authentication operations
 */
class AuthRemoteDataSourceImpl @Inject constructor(
    private val apiService: AuthApiService
) : AuthRemoteDataSource {
    
    override suspend fun login(
        email: String, 
        password: String,
        fcmToken: String? = null
    ): Result<AuthResponse> {
        return safeApiCall {
            val request = AuthRequest(email, password)
            apiService.login(request, fcmToken)
        }
    }

    override suspend fun register(
        name: String, 
        email: String, 
        password: String,
        fcmToken: String? = null
    ): Result<AuthResponse> {
        return safeApiCall {
            val request = AuthRequest(email, password)
            // Note: You might need to adjust this based on your API requirements
            apiService.register(request, name, fcmToken)
        }
    }

    override suspend fun refreshToken(refreshToken: String): Result<AuthResponse> {
        return safeApiCall {
            // Implementation depends on your API
            // Example: apiService.refreshToken(refreshToken)
            throw UnsupportedOperationException("Refresh token not implemented")
        }
    }

    override suspend fun logout() {
        // Implementation depends on your API
        // Example: apiService.logout()
    }


    override fun isLoggedIn(): Boolean {
        // This should check local storage for a valid token
        return false
    }

    override fun getAccessToken(): String? {
        // This should retrieve the token from secure storage
        return null
    }

    private suspend fun <T> safeApiCall(apiCall: suspend () -> T): Result<T> {
        return withContext(Dispatchers.IO) {
            try {
                Success(apiCall())
            } catch (throwable: Throwable) {
                Error(
                    exception = when (throwable) {
                        is IOException -> throwable
                        is HttpException -> throwable
                        else -> Exception(throwable)
                    },
                    message = when (throwable) {
                        is IOException -> "No internet connection"
                        is HttpException -> when (throwable.code()) {
                            401 -> "Unauthorized. Please login again."
                            403 -> "Forbidden. You don't have permission to access this resource."
                            404 -> "Resource not found."
                            500 -> "Internal server error. Please try again later."
                            else -> "An error occurred. Please try again."
                        }
                        else -> throwable.message ?: "An unknown error occurred"
                    }
                )
            }
        }
    }
}
