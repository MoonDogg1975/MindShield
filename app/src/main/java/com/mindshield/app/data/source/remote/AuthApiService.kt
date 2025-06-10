package com.mindshield.app.data.source.remote

import com.mindshield.app.data.model.AuthRequest
import com.mindshield.app.data.model.AuthResponse
import com.mindshield.app.data.model.BaseResponse
import com.mindshield.app.data.model.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

/**
 * Retrofit API service for authentication endpoints
 */
interface AuthApiService {
    /**
     * Login with email and password
     * @param request The login request containing email and password
     * @return AuthResponse with user data and tokens
     */
    @POST("auth/login")
    suspend fun login(@Body request: AuthRequest): Response<BaseResponse<AuthResponse>>
    
    /**
     * Register a new user
     * @param request The registration request containing user details
     * @return AuthResponse with user data and tokens
     */
    @POST("auth/register")
    suspend fun register(@Body request: AuthRequest): Response<BaseResponse<AuthResponse>>
    
    /**
     * Get the current authenticated user's profile
     * @param authorization Bearer token for authentication
     * @return User profile data
     */
    @GET("auth/me")
    suspend fun getCurrentUser(
        @Header("Authorization") authorization: String
    ): Response<BaseResponse<User>>
    
    /**
     * Refresh authentication token
     * @param refreshToken The refresh token
     * @return New access and refresh tokens
     */
    @FormUrlEncoded
    @POST("auth/refresh")
    suspend fun refreshToken(
        @Field("refresh_token") refreshToken: String
    ): Response<BaseResponse<AuthResponse>>
    
    /**
     * Logout the current user
     * @param authorization Bearer token for authentication
     * @return Success response
     */
    @POST("auth/logout")
    suspend fun logout(
        @Header("Authorization") authorization: String
    ): Response<BaseResponse<Unit>>
    
    /**
     * Request password reset
     * @param email The user's email address
     * @return Success response
     */
    @FormUrlEncoded
    @POST("auth/password/forgot")
    suspend fun forgotPassword(
        @Field("email") email: String
    ): Response<BaseResponse<Unit>>
    
    /**
     * Reset password with token
     * @param token The reset token
     * @param newPassword The new password
     * @return Success response
     */
    @FormUrlEncoded
    @POST("auth/password/reset")
    suspend fun resetPassword(
        @Field("token") token: String,
        @Field("password") newPassword: String
    ): Response<BaseResponse<Unit>>
}
