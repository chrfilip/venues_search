package com.adyen.android.assignment.data.location

import android.content.Context
import android.location.Location
import android.location.LocationManager
import androidx.core.content.ContextCompat
import androidx.core.location.LocationManagerCompat
import com.adyen.android.assignment.domain.common.Result
import java.lang.Exception

/**
 * A class for finding the user's location.
 */
class LocationRepository(
    private val locationProvider: LocationProvider,
    private val context: Context
) {

    /**
     * A method to get user's location.
     * First checks if last location is available and if not requests for new location.
     *
     * @return [Result.Success] if location found or [Result.Failed] if not.
     */
    suspend fun getLocation(): Result<Location> {
        val lastLocation = locationProvider.getLastLocation()
        return if (lastLocation == null) {
            val location = locationProvider.requestLocation()
            if (location != null) {
                Result.Success(location)
            } else {
                Result.Failed(Exception("No Location"))
            }
        } else {
            Result.Success(lastLocation)
        }
    }

    fun isLocationEnabled(): Boolean {
        val locationManager =
            ContextCompat.getSystemService(
                context,
                LocationManager::class.java
            ) as LocationManager
        return LocationManagerCompat.isLocationEnabled(locationManager)
    }
}