package com.mindshield.app.ui.utils

/**
 * A generic class that holds a value with its loading status.
 * @param T
 */
sealed class UiState<out T> {
    object Loading : UiState<Nothing>()
    data class Success<out T>(val data: T) : UiState<T>()
    data class Error(val message: String) : UiState<Nothing>()
    object Empty : UiState<Nothing>()
}

/**
 * A generic class that holds a value with its loading status for pagination.
 * @param T
 */
sealed class PagingState<out T> {
    object Loading : PagingState<Nothing>()
    data class Success<out T>(
        val data: T,
        val currentPage: Int,
        val isLastPage: Boolean = false
    ) : PagingState<T>()
    data class Error(val message: String) : PagingState<Nothing>()
}
