package com.adyen.android.assignment.data.api.repository

import com.adyen.android.assignment.data.api.common.SafeApiResponse
import com.adyen.android.assignment.data.api.common.safeApiCall
import com.adyen.android.assignment.data.api.model.ResponseWrapper
import com.adyen.android.assignment.data.api.service.PlacesService
import com.adyen.android.assignment.domain.common.Result
import javax.inject.Inject

class PlacesRepository @Inject constructor(
    private val placesService: PlacesService
) {

    suspend fun searchVenues(queryParams: Map<String, String>): Result<ResponseWrapper> {
        return when (val result = safeApiCall { placesService.getVenueRecommendations(queryParams) }) {
            is SafeApiResponse.Success -> Result.Success(result.payload)
            is SafeApiResponse.Failed -> Result.Failed(result.exception)
        }
    }
}