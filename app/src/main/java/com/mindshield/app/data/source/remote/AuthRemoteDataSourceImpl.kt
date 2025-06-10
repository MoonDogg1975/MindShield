package com.mindshield.app.data.source.remote

import com.mindshield.app.data.model.AuthRequest
import com.mindshield.app.data.model.AuthResponse
import com.mindshield.app.data.model.User
import com.mindshield.app.utils.Result
import com.mindshield.app.utils.safeApiCall
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Implementation of [AuthRemoteDataSource] that handles authentication operations with the server
 */
@Singleton
class AuthRemoteDataSourceImpl @Inject constructor(
    private val apiService: AuthApiService
) : AuthRemoteDataSource {

    override suspend fun login(
        email: String,
        password: String,
        fcmToken: String?
    ): Result<AuthResponse> = safeApiCall {
        val request = AuthRequest.login(email, password, fcmToken)
        apiService.login(request).getDataOrThrow()
    }

    override suspend fun register(
        name: String,
        email: String,
        password: String,
        fcmToken: String?
    ): Result<AuthResponse> = safeApiCall {
        val request = AuthRequest.register(name, email, password, fcmToken)
        apiService.register(request).getDataOrThrow()
    }

    override suspend fun refreshToken(refreshToken: String): Result<AuthResponse> = safeApiCall {
        apiService.refreshToken(refreshToken).getDataOrThrow()
    }

    override suspend fun logout(accessToken: String): Result<Unit> = safeApiCall {
        apiService.logout("Bearer $accessToken").getDataOrThrow()
    }

    override suspend fun getCurrentUser(accessToken: String): Result<User> = safeApiCall {
        apiService.getCurrentUser("Bearer $accessToken").getDataOrThrow()
    }

    override suspend fun forgotPassword(email: String): Result<Unit> = safeApiCall {
        apiService.forgotPassword(email).getDataOrThrow()
    }

    override suspend fun resetPassword(token: String, newPassword: String): Result<Unit> = safeApiCall {
        apiService.resetPassword(token, newPassword).getDataOrThrow()
    }

    override suspend fun updateFcmToken(accessToken: String, fcmToken: String): Result<Unit> = safeApiCall {
        // This is a simplified example - adjust according to your API
        // You might need to create a new endpoint for this or include it in another request
        Result.Success(Unit)
    }
}
