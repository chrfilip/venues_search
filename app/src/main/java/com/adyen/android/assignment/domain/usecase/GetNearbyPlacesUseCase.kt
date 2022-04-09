package com.adyen.android.assignment.domain.usecase

import com.adyen.android.assignment.api.query.VenueRecommendationsQueryBuilder
import com.adyen.android.assignment.api.repository.PlacesRepositoryImpl
import com.adyen.android.assignment.domain.Mapper
import com.adyen.android.assignment.domain.common.Result
import com.adyen.android.assignment.domain.model.Venue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetNearbyPlacesUseCase(
    private val repositoryImpl: PlacesRepositoryImpl,
    private val queryBuilder: VenueRecommendationsQueryBuilder,
    private val mapper: Mapper
) {

    suspend fun execute(input: Input): Result<List<Venue>> {
        return withContext(Dispatchers.IO) {
            val queryParams = queryBuilder.setLatitudeLongitude(input.latitude, input.longitude).build()
            when (val result = repositoryImpl.searchVenues(queryParams)) {
                is Result.Failed -> Result.Failed(result.exception)
                is Result.Success -> Result.Success(mapper.map(result.data))
            }
        }
    }

    data class Input(val longitude: Double, val latitude: Double)
}