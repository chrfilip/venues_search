package com.adyen.android.assignment.api.repository

import com.adyen.android.assignment.api.service.PlacesService
import com.adyen.android.assignment.api.query.VenueRecommendationsQueryBuilder

class PlacesRepositoryImpl(
    private val placesService: PlacesService,
    private val queryBuilder: VenueRecommendationsQueryBuilder
) : PlacesRepository {

}