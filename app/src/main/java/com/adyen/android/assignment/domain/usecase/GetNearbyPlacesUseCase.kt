package com.adyen.android.assignment.domain.usecase

import android.location.Location
import com.adyen.android.assignment.data.api.repository.PlacesRepository
import com.adyen.android.assignment.data.location.LocationRepository
import com.adyen.android.assignment.domain.model.Mapper
import com.adyen.android.assignment.domain.common.Result
import com.adyen.android.assignment.domain.model.Distance
import com.adyen.android.assignment.domain.model.Venue
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetNearbyPlacesUseCase(
    private val dispatcher: CoroutineDispatcher,
    private val locationRepository: LocationRepository,
    private val placesRepository: PlacesRepository,
    private val mapper: Mapper
) {
    suspend fun execute(): Result<List<Venue>> {
        return when (val locationResult = locationRepository.getLocation()) {
            is Result.Failed -> locationResult
            is Result.Success -> fetchNearbyPlaces(locationResult.data)
        }
    }

    private suspend fun fetchNearbyPlaces(location: Location) = withContext(dispatcher) {
        when (val result = placesRepository.getNearbyPlaces(location.latitude, location.longitude)) {
            is Result.Failed -> Result.Failed(result.exception)
            is Result.Success -> Result.Success(mapper.map(result.data))
        }
    }
}