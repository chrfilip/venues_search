package com.adyen.android.assignment.api.repository

import com.adyen.android.assignment.api.model.ResponseWrapper
import com.adyen.android.assignment.domain.common.Either

interface PlacesRepository {
    suspend fun searchVenues(queryParams: Map<String, String>): Either<ResponseWrapper>
}