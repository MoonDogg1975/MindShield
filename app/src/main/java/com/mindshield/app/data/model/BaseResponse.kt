package com.mindshield.app.data.model

import com.google.gson.annotations.SerializedName

/**
 * Base response model for all API responses
 * @param T The type of data contained in the response
 * @property success Boolean indicating if the request was successful
 * @property message Optional message from the server
 * @property data The actual data payload
 * @property code Optional status code
 */
data class BaseResponse<T>(
    @SerializedName("success")
    val success: Boolean,
    
    @SerializedName("message")
    val message: String? = null,
    
    @SerializedName("data")
    val data: T? = null,
    
    @SerializedName("code")
    val code: Int? = null
) {
    /**
     * Returns the data if the request was successful, otherwise throws an exception
     * @throws ApiException if the request was not successful
     */
    fun getDataOrThrow(): T {
        if (success) {
            return data ?: throw ApiException("No data available")
        }
        throw ApiException(message ?: "Unknown error")
    }
}

/**
 * Exception class for API errors
 */
class ApiException(override val message: String) : Exception(message)
