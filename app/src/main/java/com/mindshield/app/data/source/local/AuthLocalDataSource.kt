package com.mindshield.app.data.source.local

import android.content.Context
import com.mindshield.app.data.model.AuthResponse
import com.mindshield.app.data.source.AuthDataSource
import com.mindshield.app.utils.PreferenceManager
import com.mindshield.app.utils.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Local data source implementation for authentication operations
 */
@Singleton
class AuthLocalDataSource @Inject constructor(
    private val context: Context,
    private val preferenceManager: PreferenceManager
) : AuthDataSource {
    
    companion object {
        private const val PREF_ACCESS_TOKEN = "access_token"
        private const val PREF_REFRESH_TOKEN = "refresh_token"
        private const val PREF_USER_ID = "user_id"
        private const val PREF_USER_EMAIL = "user_email"
        private const val PREF_USER_NAME = "user_name"
        private const val PREF_IS_LOGGED_IN = "is_logged_in"
    }

    override suspend fun login(email: String, password: String): Result<AuthResponse> {
        // This is just a local data source, actual login should be handled by remote data source
        throw UnsupportedOperationException("Login not supported in local data source")
    }

    override suspend fun register(email: String, password: String, name: String): Result<AuthResponse> {
        // This is just a local data source, actual registration should be handled by remote data source
        throw UnsupportedOperationException("Register not supported in local data source")
    }

    override suspend fun refreshToken(refreshToken: String): Result<AuthResponse> {
        // This is just a local data source, actual token refresh should be handled by remote data source
        throw UnsupportedOperationException("Refresh token not supported in local data source")
    }

    override suspend fun logout() {
        withContext(Dispatchers.IO) {
            preferenceManager.clear()
        }
    }

    override fun isLoggedIn(): Boolean {
        return preferenceManager.getBoolean(PREF_IS_LOGGED_IN, false) &&
                preferenceManager.getString(PREF_ACCESS_TOKEN, null) != null
    }

    override fun getAccessToken(): String? {
        return preferenceManager.getString(PREF_ACCESS_TOKEN, null)
    }
    
    /**
     * Save authentication data to local storage
     */
    suspend fun saveAuthData(authResponse: AuthResponse) {
        withContext(Dispatchers.IO) {
            preferenceManager.apply {
                putString(PREF_ACCESS_TOKEN, authResponse.accessToken)
                putString(PREF_REFRESH_TOKEN, authResponse.refreshToken)
                putString(PREF_USER_ID, authResponse.user.id)
                putString(PREF_USER_EMAIL, authResponse.user.email)
                putString(PREF_USER_NAME, authResponse.user.name)
                putBoolean(PREF_IS_LOGGED_IN, true)
            }
        }
    }

    override suspend fun getAuthResponse(): AuthResponse? {
        return withContext(Dispatchers.IO) {
            val accessToken = preferenceManager.getString(PREF_ACCESS_TOKEN, null)
            val refreshToken = preferenceManager.getString(PREF_REFRESH_TOKEN, null)
            val userId = preferenceManager.getString(PREF_USER_ID, null)
            val userEmail = preferenceManager.getString(PREF_USER_EMAIL, null)
            val userName = preferenceManager.getString(PREF_USER_NAME, null)
            if (accessToken != null && refreshToken != null && userId != null && userEmail != null && userName != null) {
                AuthResponse(accessToken, refreshToken, User(userId, userEmail, userName))
            } else {
                null
            }
        }
    }

    override suspend fun clearAuthData() {
        withContext(Dispatchers.IO) {
            preferenceManager.clear()
        }
    }

    override suspend fun isLoggedIn(): Boolean {
        return preferenceManager.getBoolean(PREF_IS_LOGGED_IN, false) &&
                preferenceManager.getString(PREF_ACCESS_TOKEN, null) != null
    }

    override suspend fun getAccessToken(): String? {
        return preferenceManager.getString(PREF_ACCESS_TOKEN, null)
    }

    override suspend fun getRefreshToken(): String? {
        return preferenceManager.getString(PREF_REFRESH_TOKEN, null)
    }

    override suspend fun saveUser(user: User) {
        withContext(Dispatchers.IO) {
            preferenceManager.apply {
                putString(PREF_USER_ID, user.id)
                putString(PREF_USER_EMAIL, user.email)
                putString(PREF_USER_NAME, user.name)
            }
        }
    }

    override suspend fun getCurrentUser(): User? {
        return withContext(Dispatchers.IO) {
            val userId = preferenceManager.getString(PREF_USER_ID, null)
            val userEmail = preferenceManager.getString(PREF_USER_EMAIL, null)
            val userName = preferenceManager.getString(PREF_USER_NAME, null)
            if (userId != null && userEmail != null && userName != null) {
                User(userId, userEmail, userName)
            } else {
                null
            }
        }
    }

    override suspend fun saveFcmToken(token: String) {
        withContext(Dispatchers.IO) {
            preferenceManager.putString(PREF_FCM_TOKEN, token)
        }
    }

    override suspend fun getFcmToken(): String? {
        return preferenceManager.getString(PREF_FCM_TOKEN, null)
    }

    override suspend fun saveLastLoginTimestamp(timestamp: Long) {
        withContext(Dispatchers.IO) {
            preferenceManager.putLong(PREF_LAST_LOGIN_TIMESTAMP, timestamp)
        }
    }

    override suspend fun getLastLoginTimestamp(): Long {
        return preferenceManager.getLong(PREF_LAST_LOGIN_TIMESTAMP, 0)
    }

    override fun observeAuthState(): Flow<Boolean> {
        return flow {
            while (true) {
                val isLoggedIn = isLoggedIn()
                emit(isLoggedIn)
                delay(1000)
            }
        }
    }

    override fun observeCurrentUser(): Flow<User?> {
        return flow {
            while (true) {
                val user = getCurrentUser()
                emit(user)
                delay(1000)
            }
        }
    }
}
