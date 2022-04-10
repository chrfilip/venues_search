package com.adyen.android.assignment

import com.adyen.android.assignment.data.api.query.VenueRecommendationsQueryBuilder
import com.adyen.android.assignment.data.api.repository.PlacesRepository
import com.adyen.android.assignment.data.api.service.PlacesService
import com.adyen.android.assignment.domain.common.Result
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import retrofit2.Response

@ExperimentalCoroutinesApi
@ExtendWith(CoroutineScopeExtension::class)
class PlacesRepositoryTest {

    private val service = mock<PlacesService>()
    private val queryBuilder = mock<VenueRecommendationsQueryBuilder>()
    private lateinit var repository: PlacesRepository

    @BeforeEach
    fun setUp() {
        whenever(queryBuilder.setLatitudeLongitude(any(), any())).thenReturn(mock())
        whenever(queryBuilder.build()).thenReturn(mock())
        repository = PlacesRepository(service, queryBuilder)
    }

    @Test
    fun `when search api call fails then result should be failure`() = runBlockingTest {
            whenever(service.getVenueRecommendations(anyNonNull())).thenReturn(
                Response.error(
                    400,
                    "error".toResponseBody()
                )
            )

            val result = repository.getNearbyPlaces(1.2, 3.4)

            assertTrue(result is Result.Failed)
        }

    @Test
    fun `when search api call succeeds then result should be returned`() = runBlockingTest {
            whenever(service.getVenueRecommendations(anyNonNull())).thenReturn(
                Response.success(
                    venueSearchSuccessResponse
                )
            )

            val result = repository.getNearbyPlaces(1.2, 3.4)

            assertEquals(4, (result as Result.Success).data.results?.size)
        }
}