package com.adyen.android.assignment.api.repository

import com.adyen.android.assignment.api.common.SafeApiResponse
import com.adyen.android.assignment.api.common.safeApiCall
import com.adyen.android.assignment.api.model.ResponseWrapper
import com.adyen.android.assignment.api.service.PlacesService
import com.adyen.android.assignment.domain.common.Either
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PlacesRepositoryImpl @Inject constructor(
    private val placesService: PlacesService
) : PlacesRepository {

    override suspend fun searchVenues(queryParams: Map<String, String>): Either<ResponseWrapper> {
        return withContext(Dispatchers.IO) {
            when (val result = safeApiCall { placesService.getVenueRecommendations(queryParams) }) {
                is SafeApiResponse.Success -> Either.Success(result.payload)
                is SafeApiResponse.Failed -> Either.Failed(result.exception)
            }
        }
    }
}