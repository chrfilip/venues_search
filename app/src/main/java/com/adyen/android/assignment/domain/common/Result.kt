package com.adyen.android.assignment.domain.common

/**
 * Wrapper for Success/Error results
 */
sealed class Result<out T : Any> {

    data class Success<out T : Any>(val data: T) : Result<T>()

    data class Failed(val exception: Exception) : Result<Nothing>()
}