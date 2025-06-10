package com.mindshield.app.utils

/**
 * A generic class that holds a value with its loading status.
 * @param <T>
 */
sealed class Result<out T> {
    data class Success<out T>(val data: T) : Result<T>() {
        val isSuccess = true
    }

    data class Error(
        val exception: Exception,
        val message: String = exception.localizedMessage ?: "An unknown error occurred"
    ) : Result<Nothing>()

    object Loading : Result<Nothing>() {
        val isLoading = true
    }

    object Empty : Result<Nothing>()

    /**
     * Returns `true` if this instance represents a successful outcome.
     */
    val isSuccess: Boolean
        get() = this is Success && data != null

    /**
     * Returns `true` if this instance represents a failed outcome.
     */
    val isError: Boolean
        get() = this is Error

    /**
     * Returns `true` if this instance represents a loading state.
     */
    val isLoading: Boolean
        get() = this is Loading

    /**
     * Returns the encapsulated result if this instance represents [Success] or `null` if it is error or loading.
     */
    fun getOrNull(): T? = (this as? Success)?.data

    /**
     * Returns the encapsulated exception if this instance represents [Error] or `null` if it is success or loading.
     */
    fun exceptionOrNull(): Throwable? = (this as? Error)?.exception
}
