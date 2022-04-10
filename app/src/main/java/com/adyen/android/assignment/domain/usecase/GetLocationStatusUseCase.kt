package com.adyen.android.assignment.domain.usecase

import com.adyen.android.assignment.data.location.LocationRepository

class GetLocationStatusUseCase(private val locationRepository: LocationRepository) {
    fun execute() = locationRepository.isLocationEnabled()
}