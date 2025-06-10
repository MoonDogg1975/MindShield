package com.mindshield.app.data.source.local

import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import com.mindshield.app.data.model.AuthResponse
import com.mindshield.app.data.model.User
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthLocalDataSourceImpl @Inject constructor(
    private val encryptedPrefs: EncryptedSharedPreferences,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : AuthLocalDataSource {

    companion object {
        private const val PREF_ACCESS_TOKEN = "pref_access_token"
        private const val PREF_REFRESH_TOKEN = "pref_refresh_token"
        private const val PREF_USER_ID = "pref_user_id"
        private const val PREF_USER_EMAIL = "pref_user_email"
        private const val PREF_USER_NAME = "pref_user_name"
        private const val PREF_IS_LOGGED_IN = "pref_is_logged_in"
        private const val PREF_FCM_TOKEN = "pref_fcm_token"
        private const val PREF_LAST_LOGIN_TIMESTAMP = "pref_last_login_timestamp"
    }

    private val prefs: SharedPreferences = encryptedPrefs
    private val editor = prefs.edit()

    override suspend fun saveAuthResponse(authResponse: AuthResponse) = withContext(ioDispatcher) {
        editor.apply {
            putString(PREF_ACCESS_TOKEN, authResponse.accessToken)
            putString(PREF_REFRESH_TOKEN, authResponse.refreshToken)
            putString(PREF_USER_ID, authResponse.user.id)
            putString(PREF_USER_EMAIL, authResponse.user.email)
            putString(PREF_USER_NAME, authResponse.user.name)
            putBoolean(PREF_IS_LOGGED_IN, true)
            apply()
        }
    }

    override suspend fun getAuthResponse(): AuthResponse? = withContext(ioDispatcher) {
        val accessToken = prefs.getString(PREF_ACCESS_TOKEN, null)
        val refreshToken = prefs.getString(PREF_REFRESH_TOKEN, null)
        val userId = prefs.getString(PREF_USER_ID, null)
        val userEmail = prefs.getString(PREF_USER_EMAIL, null)
        val userName = prefs.getString(PREF_USER_NAME, null)

        if (accessToken != null && refreshToken != null && userId != null && 
            userEmail != null && userName != null) {
            AuthResponse(
                accessToken = accessToken,
                refreshToken = refreshToken,
                user = User(
                    id = userId,
                    name = userName,
                    email = userEmail
                )
            )
        } else {
            null
        }
    }

    override suspend fun clearAuthData() = withContext(ioDispatcher) {
        editor.clear().apply()
    }

    override suspend fun isLoggedIn(): Boolean = withContext(ioDispatcher) {
        prefs.getBoolean(PREF_IS_LOGGED_IN, false) && 
        prefs.getString(PREF_ACCESS_TOKEN, null) != null
    }

    override suspend fun getAccessToken(): String? = withContext(ioDispatcher) {
        prefs.getString(PREF_ACCESS_TOKEN, null)
    }

    override suspend fun getRefreshToken(): String? = withContext(ioDispatcher) {
        prefs.getString(PREF_REFRESH_TOKEN, null)
    }

    override suspend fun saveUser(user: User) = withContext(ioDispatcher) {
        editor.apply {
            putString(PREF_USER_ID, user.id)
            putString(PREF_USER_EMAIL, user.email)
            putString(PREF_USER_NAME, user.name)
            apply()
        }
    }

    override suspend fun getCurrentUser(): User? = withContext(ioDispatcher) {
        val userId = prefs.getString(PREF_USER_ID, null)
        val userEmail = prefs.getString(PREF_USER_EMAIL, null)
        val userName = prefs.getString(PREF_USER_NAME, null)

        if (userId != null && userEmail != null && userName != null) {
            User(
                id = userId,
                name = userName,
                email = userEmail
            )
        } else {
            null
        }
    }

    override suspend fun saveFcmToken(token: String) = withContext(ioDispatcher) {
        editor.putString(PREF_FCM_TOKEN, token).apply()
    }

    override suspend fun getFcmToken(): String? = withContext(ioDispatcher) {
        prefs.getString(PREF_FCM_TOKEN, null)
    }

    override suspend fun saveLastLoginTimestamp(timestamp: Long) = withContext(ioDispatcher) {
        editor.putLong(PREF_LAST_LOGIN_TIMESTAMP, timestamp).apply()
    }

    override suspend fun getLastLoginTimestamp(): Long = withContext(ioDispatcher) {
        prefs.getLong(PREF_LAST_LOGIN_TIMESTAMP, 0)
    }

    override fun observeAuthState(): Flow<Boolean> = flow {
        while (true) {
            emit(isLoggedIn())
            kotlinx.coroutines.delay(1000) // Poll every second
        }
    }.flowOn(ioDispatcher)


    override fun observeCurrentUser(): Flow<User?> = flow {
        while (true) {
            emit(getCurrentUser())
            kotlinx.coroutines.delay(1000) // Poll every second
        }
    }.flowOn(ioDispatcher)
}
