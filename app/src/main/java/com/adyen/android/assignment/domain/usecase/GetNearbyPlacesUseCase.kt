package com.adyen.android.assignment.domain.usecase

import android.location.Location
import com.adyen.android.assignment.data.api.query.VenueRecommendationsQueryBuilder
import com.adyen.android.assignment.data.api.repository.PlacesRepository
import com.adyen.android.assignment.data.location.LocationRepository
import com.adyen.android.assignment.domain.model.Mapper
import com.adyen.android.assignment.domain.common.Result
import com.adyen.android.assignment.domain.model.Venue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetNearbyPlacesUseCase(
    private val locationRepository: LocationRepository,
    private val placesRepository: PlacesRepository,
    private val queryBuilder: VenueRecommendationsQueryBuilder,
    private val mapper: Mapper
) {
    suspend fun execute(): Result<List<Venue>> {
        return when (val locationResult = locationRepository.getLocation()) {
            is Result.Failed -> locationResult
            is Result.Success -> fetchNearbyPlaces(locationResult.data)
        }
    }

    private suspend fun fetchNearbyPlaces(location: Location) = withContext(Dispatchers.IO) {
        val queryParams = queryBuilder.setLatitudeLongitude(location.latitude, location.longitude).build()
        when (val result = placesRepository.searchVenues(queryParams)) {
            is Result.Failed -> Result.Failed(result.exception)
            is Result.Success -> Result.Success(mapper.map(result.data))
        }
    }
}