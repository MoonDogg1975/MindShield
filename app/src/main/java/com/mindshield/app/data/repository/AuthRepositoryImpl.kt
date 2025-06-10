package com.mindshield.app.data.repository

import com.mindshield.app.data.model.AuthResponse
import com.mindshield.app.data.source.local.AuthLocalDataSource
import com.mindshield.app.data.source.remote.AuthRemoteDataSource
import com.mindshield.app.utils.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Implementation of [AuthRepository] that coordinates between local and remote data sources
 */
@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val localDataSource: AuthLocalDataSource,
    private val remoteDataSource: AuthRemoteDataSource
) : AuthRepository {

    override suspend fun login(email: String, password: String): Result<Unit> {
        return try {
            // Get FCM token if available
            val fcmToken = localDataSource.getFcmToken()
            
            // Call remote data source
            val result = remoteDataSource.login(email, password, fcmToken)
            
            when (result) {
                is Result.Success -> {
                    // Save auth data locally
                    localDataSource.saveAuthResponse(result.data)
                    localDataSource.saveUser(result.data.user)
                    localDataSource.saveLastLoginTimestamp(System.currentTimeMillis())
                    
                    // Update FCM token on server if available
                    fcmToken?.let { token ->
                        remoteDataSource.updateFcmToken(result.data.accessToken, token)
                    }
                    
                    Result.Success(Unit)
                }
                is Result.Error -> result
                is Result.Loading -> Result.Success(Unit) // Should not happen
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun register(
        name: String,
        email: String,
        password: String
    ): Result<Unit> {
        return try {
            // Get FCM token if available
            val fcmToken = localDataSource.getFcmToken()
            
            // Call remote data source
            val result = remoteDataSource.register(name, email, password, fcmToken)
            
            when (result) {
                is Result.Success -> {
                    // Save auth data locally
                    localDataSource.saveAuthResponse(result.data)
                    localDataSource.saveUser(result.data.user)
                    localDataSource.saveLastLoginTimestamp(System.currentTimeMillis())
                    
                    // Update FCM token on server if available
                    fcmToken?.let { token ->
                        remoteDataSource.updateFcmToken(result.data.accessToken, token)
                    }
                    
                    Result.Success(Unit)
                }
                is Result.Error -> result
                is Result.Loading -> Result.Success(Unit) // Should not happen
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun logout(): Result<Unit> {
        return try {
            // Get current access token
            val accessToken = localDataSource.getAccessToken()
            
            // Call remote logout if we have a token
            accessToken?.let {
                remoteDataSource.logout(it)
            }
            
            // Clear local data in any case
            localDataSource.clearAuthData()
            
            Result.Success(Unit)
        } catch (e: Exception) {
            // Even if remote logout fails, clear local data
            localDataSource.clearAuthData()
            Result.Error(e)
        }
    }

    override suspend fun refreshToken(): Result<Unit> {
        return try {
            // Get current refresh token
            val refreshToken = localDataSource.getRefreshToken()
                ?: return Result.Error(Exception("No refresh token available"))
            
            // Call remote data source
            val result = remoteDataSource.refreshToken(refreshToken)
            
            when (result) {
                is Result.Success -> {
                    // Save new tokens
                    localDataSource.saveAuthResponse(result.data)
                    Result.Success(Unit)
                }
                is Result.Error -> {
                    // If refresh fails, log out the user
                    localDataSource.clearAuthData()
                    result
                }
                is Result.Loading -> Result.Success(Unit) // Should not happen
            }
        } catch (e: Exception) {
            // If any error occurs, log out the user
            localDataSource.clearAuthData()
            Result.Error(e)
        }
    }

    override suspend fun isLoggedIn(): Boolean {
        return localDataSource.isLoggedIn()
    }

    override fun observeAuthState(): Flow<Boolean> {
        return localDataSource.observeAuthState()
    }

    override fun observeCurrentUser(): Flow<com.mindshield.app.data.model.User?> {
        return localDataSource.observeCurrentUser()
    }

    override suspend fun getCurrentUser(): com.mindshield.app.data.model.User? {
        return localDataSource.getCurrentUser()
    }

    override suspend fun saveFcmToken(token: String) {
        localDataSource.saveFcmToken(token)
    }

    override suspend fun forgotPassword(email: String): Result<Unit> {
        return try {
            remoteDataSource.forgotPassword(email)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun resetPassword(token: String, newPassword: String): Result<Unit> {
        return try {
            remoteDataSource.resetPassword(token, newPassword)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}
