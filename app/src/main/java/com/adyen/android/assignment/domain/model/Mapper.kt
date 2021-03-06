package com.adyen.android.assignment.domain.model

import com.adyen.android.assignment.data.api.model.ResponseWrapper
import com.adyen.android.assignment.data.api.model.Result

class Mapper {

    fun map(responseWrapper: ResponseWrapper): List<Venue> {
        return responseWrapper.results?.map {
            it.toVenue()
        } ?: emptyList()
    }

    private fun Result.toVenue() = Venue(
        name = name,
        distance = getDistance(distance),
        address = getAddress(location.address, location.postcode)
    )

    private fun getDistance(meters: Int) = when (meters) {
        in 0 until 1000 -> Distance.Meters(meters)
        else -> Distance.KiloMeters(meters / 1000f)
    }

    private fun getAddress(address: String?, postCode: String?): String {
        return if (address.isNullOrEmpty() && postCode.isNullOrEmpty()) {
            "Unknown address"
        } else {
            listOf(address, postCode).filter { !it.isNullOrEmpty() }.joinToString(", ")
        }
    }
}