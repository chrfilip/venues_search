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
        distance = distance,
        address = "${location.address}, ${location.postcode}"
    )
}