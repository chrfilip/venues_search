package com.adyen.android.assignment.domain

import com.adyen.android.assignment.api.model.ResponseWrapper
import com.adyen.android.assignment.api.model.Result
import com.adyen.android.assignment.domain.model.Venue

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