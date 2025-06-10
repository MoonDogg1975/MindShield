package com.mindshield.app.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import java.util.Date

/**
 * Data class representing the authentication response
 */
@Parcelize
data class AuthResponse(
    @SerializedName("access_token")
    val accessToken: String,
    
    @SerializedName("refresh_token")
    val refreshToken: String,
    
    @SerializedName("token_type")
    val tokenType: String = "Bearer",
    
    @SerializedName("expires_in")
    val expiresIn: Long,
    
    @SerializedName("user")
    val user: User
) : Parcelable {
    /**
     * Get the expiration date of the access token
     * @return Date when the token will expire
     */
    fun getExpirationDate(): Date {
        val currentTime = System.currentTimeMillis()
        return Date(currentTime + expiresIn * 1000)
    }
    
    /**
     * Check if the token is expired
     * @return true if the token is expired, false otherwise
     */
    fun isExpired(): Boolean {
        return System.currentTimeMillis() >= getExpirationDate().time
    }
}

/**
 * Data class representing user information
 */
@Parcelize
data class User(
    @SerializedName("id")
    val id: String,
    
    @SerializedName("name")
    val name: String,
    
    @SerializedName("email")
    val email: String,
    
    @SerializedName("email_verified_at")
    val emailVerifiedAt: String? = null,
    
    @SerializedName("phone_number")
    val phoneNumber: String? = null,
    
    @SerializedName("avatar_url")
    val avatarUrl: String? = null,
    
    @SerializedName("role")
    val role: String,
    
    @SerializedName("status")
    val status: String,
    
    @SerializedName("created_at")
    val createdAt: String,
    
    @SerializedName("updated_at")
    val updatedAt: String
) : Parcelable {
    /**
     * Check if the user's email is verified
     * @return true if the email is verified, false otherwise
     */
    fun isEmailVerified(): Boolean = !emailVerifiedAt.isNullOrEmpty()
    
    /**
     * Check if the user has admin role
     * @return true if the user is an admin, false otherwise
     */
    fun isAdmin(): Boolean = role.equals("admin", ignoreCase = true)
}
