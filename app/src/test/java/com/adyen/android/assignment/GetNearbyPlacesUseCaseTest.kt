package com.adyen.android.assignment

import com.adyen.android.assignment.data.api.repository.PlacesRepository
import com.adyen.android.assignment.data.location.LocationRepository
import com.adyen.android.assignment.domain.common.Result
import com.adyen.android.assignment.domain.model.Distance
import com.adyen.android.assignment.domain.model.Mapper
import com.adyen.android.assignment.domain.model.Venue
import com.adyen.android.assignment.domain.usecase.GetNearbyPlacesUseCase
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExperimentalCoroutinesApi
@ExtendWith(CoroutineScopeExtension::class)
class GetNearbyPlacesUseCaseTest {

    private val testDispatcher = TestCoroutineDispatcher()
    private val placesRepository: PlacesRepository = mock()
    private val locationRepository: LocationRepository = mock()
    private val mapper: Mapper = mock()

    private lateinit var useCase: GetNearbyPlacesUseCase

    @BeforeEach
    fun setUp() {
        useCase = GetNearbyPlacesUseCase(testDispatcher, locationRepository, placesRepository, mapper)
    }

    @Test
    fun `when both location and api call succeed then result should be success`() =
        runBlockingTest {
            val venues = listOf(Venue("restaurant", Distance.Meters(1), "1234 AB"))
            whenever(
                placesRepository.getNearbyPlaces(
                    any(),
                    any()
                )
            ).thenReturn(Result.Success(mock()))
            whenever(locationRepository.getLocation()).thenReturn(Result.Success(mock()))
            whenever(mapper.map(any())).thenReturn(venues)

            val result = useCase.execute()

            assertEquals(venues, (result as Result.Success).data)
        }

    @Test
    fun `when location fails and api call succeeds then result should be error`() =
        runBlockingTest {
            whenever(
                placesRepository.getNearbyPlaces(
                    any(),
                    any()
                )
            ).thenReturn(Result.Success(mock()))
            whenever(locationRepository.getLocation()).thenReturn(Result.Failed(mock()))

            val result = useCase.execute()

            assertTrue(result is Result.Failed)
        }

    @Test
    fun `when location succeeds and api call fails then result should be error`() =
        runBlockingTest {
            whenever(
                placesRepository.getNearbyPlaces(
                    any(),
                    any()
                )
            ).thenReturn(Result.Failed(mock()))
            whenever(locationRepository.getLocation()).thenReturn(Result.Success(mock()))

            val result = useCase.execute()

            assertTrue(result is Result.Failed)
        }

}