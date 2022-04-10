package com.adyen.android.assignment.data.api.common

sealed class SafeApiResponse<out T : Any> {

    data class Success<out T : Any>(val payload: T) : SafeApiResponse<T>()

    data class Failed(val exception: Exception) : SafeApiResponse<Nothing>()

}