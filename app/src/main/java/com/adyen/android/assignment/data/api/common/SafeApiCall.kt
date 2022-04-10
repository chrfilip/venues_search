package com.adyen.android.assignment.data.api.common

import retrofit2.Response
import kotlin.Exception

suspend fun <T: Any> safeApiCall(call: suspend () -> Response<T>): SafeApiResponse<T> {
    val response: Response<T>
    try {
        response = call.invoke()
    } catch (e: Exception) {
        return SafeApiResponse.Failed(e)
    }

    return if (response.isSuccessful) {
        response.body()?.let {
            SafeApiResponse.Success(it)
        } ?: SafeApiResponse.Failed(Exception("oops, something went wrong"))
    } else {
        SafeApiResponse.Failed(Exception(response.errorBody().toString()))
    }
}