package com.mindshield.app.data.model

import com.google.gson.annotations.SerializedName

/**
 * Data class representing the authentication request
 */
data class AuthRequest(
    @SerializedName("email")
    val email: String,
    
    @SerializedName("password")
    val password: String,
    
    @SerializedName("name")
    val name: String? = null,
    
    @SerializedName("device_name")
    val deviceName: String = "Android Device",
    
    @SerializedName("fcm_token")
    val fcmToken: String? = null,
    
    @SerializedName("device_id")
    val deviceId: String = ""
) {
    companion object {
        /**
         * Create a login request
         */
        fun login(email: String, password: String, fcmToken: String? = null): AuthRequest {
            return AuthRequest(
                email = email,
                password = password,
                fcmToken = fcmToken
            )
        }
        
        /**
         * Create a registration request
         */
        fun register(
            name: String,
            email: String,
            password: String,
            fcmToken: String? = null
        ): AuthRequest {
            return AuthRequest(
                email = email,
                password = password,
                name = name,
                fcmToken = fcmToken
            )
        }
    }
}
