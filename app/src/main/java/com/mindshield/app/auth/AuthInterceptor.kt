package com.mindshield.app.auth

import android.util.Log
import com.mindshield.app.BuildConfig
import com.mindshield.app.utils.NetworkUtils
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

private const val TAG = "AuthInterceptor"
private const val HEADER_AUTHORIZATION = "Authorization"
private const val HEADER_BEARER = "Bearer"

/**
 * Interceptor that adds the authorization token to requests
 */
@Singleton
class AuthInterceptor @Inject constructor(
    private val tokenManager: TokenManager,
    private val networkUtils: NetworkUtils
) : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        
        // Skip for login and register endpoints
        if (shouldSkipAuth(originalRequest.url().toString())) {
            return chain.proceed(originalRequest)
        }
        
        // Check network connection
        if (!networkUtils.isNetworkAvailable()) {
            throw IOException("No network available")
        }
        
        // Get the token (blocking call since we're in an interceptor)
        val token = runBlocking {
            tokenManager.getValidToken()
        }
        
        // If no token, proceed without authorization
        if (token.isNullOrEmpty()) {
            if (BuildConfig.DEBUG) {
                Log.d(TAG, "No auth token available, proceeding without authorization")
            }
            return chain.proceed(originalRequest)
        }
        
        // Add the token to the request
        val authenticatedRequest = originalRequest.newBuilder()
            .header(HEADER_AUTHORIZATION, "$HEADER_BEARER $token")
            .build()
        
        // Execute the request
        val response = chain.proceed(authenticatedRequest)
        
        // Check for 401 Unauthorized
        if (response.code() == 401) {
            response.close()
            
            // Try to refresh the token
            val refreshResult = runBlocking {
                tokenManager.refreshToken()
            }
            
            return when (refreshResult) {
                is Result.Success -> {
                    // Retry the request with new token
                    val newToken = runBlocking {
                        tokenManager.getValidToken()
                    }
                    
                    if (!newToken.isNullOrEmpty()) {
                        val newRequest = originalRequest.newBuilder()
                            .header(HEADER_AUTHORIZATION, "$HEADER_BEARER $newToken")
                            .build()
                        chain.proceed(newRequest)
                    } else {
                        // If we still don't have a token after refresh, proceed without auth
                        chain.proceed(originalRequest)
                    }
                }
                is Result.Error -> {
                    // If refresh failed, clear tokens and proceed without auth
                    tokenManager.clearTokens()
                    chain.proceed(originalRequest)
                }
                else -> chain.proceed(originalRequest)
            }
        }
        
        return response
    }
    
    /**
     * Check if the request should skip authentication
     */
    private fun shouldSkipAuth(url: String): Boolean {
        val skipPaths = listOf(
            "login",
            "register",
            "forgot-password",
            "reset-password",
            "refresh-token"
        )
        
        return skipPaths.any { url.contains(it, ignoreCase = true) }
    }
}
