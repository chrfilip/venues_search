package com.adyen.android.assignment.domain.common

/**
 * Wrapper for Success/Error results
 */
sealed class Either<out T : Any> {

    data class Success<out T : Any>(val payload: T?) : Either<T>()

    data class Failed(val exception: Exception) : Either<Nothing>()
}