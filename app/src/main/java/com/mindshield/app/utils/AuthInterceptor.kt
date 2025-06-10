package com.mindshield.app.utils

import com.mindshield.app.data.source.local.AuthLocalDataSource
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

/**
 * Interceptor to add auth token to requests
 */
class AuthInterceptor @Inject constructor(
    private val authLocalDataSource: AuthLocalDataSource
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        
        // Skip adding auth header for login/register endpoints
        if (original.url.encodedPath.contains("auth/login") || 
            original.url.encodedPath.contains("auth/register")) {
            return chain.proceed(original)
        }

        // Get the token from local data source
        val token = runBlocking {
            authLocalDataSource.getAccessToken()
        }

        // If no token is available, proceed without auth header
        if (token.isNullOrEmpty()) {
            return chain.proceed(original)
        }

        // Add the auth header
        val request = original.newBuilder()
            .header("Authorization", "Bearer $token")
            .method(original.method, original.body)
            .build()

        return chain.proceed(request)
    }
}
