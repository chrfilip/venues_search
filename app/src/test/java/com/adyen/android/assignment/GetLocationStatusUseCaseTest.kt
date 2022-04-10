package com.adyen.android.assignment

import com.adyen.android.assignment.data.location.LocationRepository
import com.adyen.android.assignment.domain.usecase.GetLocationStatusUseCase
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class GetLocationStatusUseCaseTest {

    private val locationRepository: LocationRepository = mock()
    private lateinit var useCase: GetLocationStatusUseCase

    @BeforeEach
    fun setUp() {
        useCase = GetLocationStatusUseCase(locationRepository)
    }

    @Test
    fun testExecute() {
        whenever(locationRepository.isLocationEnabled()).thenReturn(false)
        assertFalse(useCase.execute())

        whenever(locationRepository.isLocationEnabled()).thenReturn(true)
        assertTrue(useCase.execute())
    }
}