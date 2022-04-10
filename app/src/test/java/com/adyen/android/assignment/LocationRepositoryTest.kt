package com.adyen.android.assignment

import android.content.Context
import android.location.LocationManager
import androidx.core.location.LocationManagerCompat
import com.adyen.android.assignment.data.location.LocationProvider
import com.adyen.android.assignment.data.location.LocationRepository
import com.adyen.android.assignment.domain.common.Result
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.MockedStatic
import org.mockito.Mockito

@ExperimentalCoroutinesApi
@ExtendWith(CoroutineScopeExtension::class)
class LocationRepositoryTest {

    private val locationProvider: LocationProvider = mock()
    private val context: Context = mock()

    private val staticMocks = mutableListOf<MockedStatic<out Any>>()

    private lateinit var repository: LocationRepository

    @BeforeEach
    fun setUp() {
        staticMocks += Mockito.mockStatic(LocationManagerCompat::class.java)
        repository = LocationRepository(locationProvider, context)
    }

    @AfterEach
    fun tearDown() {
        staticMocks.forEach {
            it.close()
        }
    }

    @Test
    fun `when both getLastLocation and requestLocation fail then should return error LocationUnknown`() = runBlockingTest {
            whenever(locationProvider.getLastLocation()).thenReturn(null)
            whenever(locationProvider.requestLocation()).thenReturn(null)

            val result = repository.getLocation()

            assertTrue(result is Result.Failed)
        }

    @Test
    fun `when getLastLocation fails and requestLocation succeeds then should return success`() = runBlockingTest {
            whenever(locationProvider.getLastLocation()).thenReturn(null)
            whenever(locationProvider.requestLocation()).thenReturn(mock())

            val result = repository.getLocation()

            assertTrue(result is Result.Success)
        }

    @Test
    fun `when getLastLocation succeeds and requestLocation fails then should return success`() = runBlockingTest {
            whenever(locationProvider.getLastLocation()).thenReturn(mock())
            whenever(locationProvider.requestLocation()).thenReturn(null)

            val result = repository.getLocation()

            assertTrue(result is Result.Success)
        }

    @Test
    fun testEnabledLocation() {
        val locationManager = mock<LocationManager>()
        whenever(context.getSystemService(any())).thenReturn(locationManager)
        whenever(LocationManagerCompat.isLocationEnabled(locationManager)).thenReturn(true)

        assertTrue(repository.isLocationEnabled())
    }

    @Test
    fun testDisabledLocation() {
        val locationManager = mock<LocationManager>()
        whenever(context.getSystemService(any())).thenReturn(locationManager)
        whenever(LocationManagerCompat.isLocationEnabled(locationManager)).thenReturn(false)

        assertFalse(repository.isLocationEnabled())
    }

}