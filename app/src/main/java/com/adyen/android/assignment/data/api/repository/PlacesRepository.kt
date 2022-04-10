package com.adyen.android.assignment.data.api.repository

import com.adyen.android.assignment.data.api.common.SafeApiResponse
import com.adyen.android.assignment.data.api.common.safeApiCall
import com.adyen.android.assignment.data.api.model.ResponseWrapper
import com.adyen.android.assignment.data.api.query.VenueRecommendationsQueryBuilder
import com.adyen.android.assignment.data.api.service.PlacesService
import com.adyen.android.assignment.domain.common.Result
import javax.inject.Inject

class PlacesRepository @Inject constructor(
    private val placesService: PlacesService,
    private val queryBuilder: VenueRecommendationsQueryBuilder
) {
    suspend fun getNearbyPlaces(latitude: Double, longitude: Double): Result<ResponseWrapper> {
        val queryParams = queryBuilder.setLatitudeLongitude(latitude, longitude).build()
        return when (val result = safeApiCall { placesService.getVenueRecommendations(queryParams) }) {
            is SafeApiResponse.Success -> Result.Success(result.payload)
            is SafeApiResponse.Failed -> Result.Failed(result.exception)
        }
    }
}