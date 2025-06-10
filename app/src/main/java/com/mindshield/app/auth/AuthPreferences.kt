package com.mindshield.app.auth

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.mindshield.app.BuildConfig
import com.mindshield.app.data.model.AuthResponse
import com.mindshield.app.data.model.User
import com.mindshield.app.utils.Result
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Manages authentication-related preferences using EncryptedSharedPreferences
 */
@Singleton
class AuthPreferences @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val masterKey by lazy {
        MasterKey.Builder(context, "mindshield_auth_master_key")
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()
    }

    private val prefs: SharedPreferences by lazy {
        EncryptedSharedPreferences.create(
            context,
            "mindshield_auth_prefs",
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    suspend fun saveAuthData(authResponse: AuthResponse) = withContext(Dispatchers.IO) {
        prefs.edit {
            putString(KEY_ACCESS_TOKEN, authResponse.accessToken)
            putString(KEY_REFRESH_TOKEN, authResponse.refreshToken)
            putBoolean(KEY_IS_LOGGED_IN, true)
            
            // Save user data
            authResponse.user?.let { user ->
                putString(KEY_USER_ID, user.id)
                putString(KEY_USER_EMAIL, user.email)
                putString(KEY_USER_NAME, user.name)
                // Add other user fields as needed
            }
            
            // Save timestamp
            putLong(KEY_LAST_LOGIN, System.currentTimeMillis())
            
            // Commit the changes
            apply()
        }
    }

    suspend fun clearAuthData() = withContext(Dispatchers.IO) {
        prefs.edit {
            remove(KEY_ACCESS_TOKEN)
            remove(KEY_REFRESH_TOKEN)
            remove(KEY_IS_LOGGED_IN)
            remove(KEY_USER_ID)
            remove(KEY_USER_EMAIL)
            remove(KEY_USER_NAME)
            remove(KEY_LAST_LOGIN)
            apply()
        }
    }

    suspend fun getAccessToken(): String? = withContext(Dispatchers.IO) {
        prefs.getString(KEY_ACCESS_TOKEN, null)
    }

    suspend fun getRefreshToken(): String? = withContext(Dispatchers.IO) {
        prefs.getString(KEY_REFRESH_TOKEN, null)
    }

    suspend fun isLoggedIn(): Boolean = withContext(Dispatchers.IO) {
        prefs.getBoolean(KEY_IS_LOGGED_IN, false) && getAccessToken() != null
    }

    suspend fun getCurrentUser(): User? = withContext(Dispatchers.IO) {
        val id = prefs.getString(KEY_USER_ID, null) ?: return@withContext null
        val email = prefs.getString(KEY_USER_EMAIL, null) ?: return@withContext null
        val name = prefs.getString(KEY_USER_NAME, "") ?: ""
        
        User(
            id = id,
            email = email,
            name = name
            // Add other user fields as needed
        )
    }

    suspend fun saveFcmToken(token: String) = withContext(Dispatchers.IO) {
        prefs.edit {
            putString(KEY_FCM_TOKEN, token)
            apply()
        }
    }

    suspend fun getFcmToken(): String? = withContext(Dispatchers.IO) {
        prefs.getString(KEY_FCM_TOKEN, null)
    }

    companion object {
        private const val KEY_ACCESS_TOKEN = "access_token"
        private const val KEY_REFRESH_TOKEN = "refresh_token"
        private const val KEY_IS_LOGGED_IN = "is_logged_in"
        private const val KEY_USER_ID = "user_id"
        private const val KEY_USER_EMAIL = "user_email"
        private const val KEY_USER_NAME = "user_name"
        private const val KEY_FCM_TOKEN = "fcm_token"
        private const val KEY_LAST_LOGIN = "last_login"
    }
}
