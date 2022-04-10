package com.adyen.android.assignment.data.location

import android.annotation.SuppressLint
import android.location.Location
import android.os.Looper
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withTimeoutOrNull
import kotlin.coroutines.resume

/**
 * A class providing location data using [FusedLocationProviderClient].
 */
class LocationProvider(
    private val fusedLocationProviderClient: FusedLocationProviderClient
) {

    private val timeout = 10000L
    private val locationRequest = LocationRequest.create()

    init {
        locationRequest.numUpdates = 1
        locationRequest.interval = 1000
        locationRequest.priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
    }

    /**
     * A method to get the last known [Location].
     * @return null if no location is cached.
     */
    @SuppressLint("MissingPermission")
    suspend fun getLastLocation(): Location? {
        return withTimeoutOrNull(timeout) {
            suspendCancellableCoroutine { cont ->
                fusedLocationProviderClient.lastLocation
                    .addOnSuccessListener { location: Location? ->
                        cont.resume(location)
                    }
                    .addOnFailureListener {
                        cont.resume(null)
                    }
                cont.invokeOnCancellation {
                    cont.resume(null)
                }
            }
        }
    }

    /**
     * A method to request for [Location].
     * @return null if no location found.
     */
    @SuppressLint("MissingPermission")
    suspend fun requestLocation(): Location? {
        return withTimeoutOrNull(timeout) {
            suspendCancellableCoroutine { cont ->

                val locationCallback: LocationCallback = object : LocationCallback() {
                    override fun onLocationResult(result: LocationResult) {
                        super.onLocationResult(result)
                        fusedLocationProviderClient.removeLocationUpdates(this)
                        val location = result.locations.filterNotNull().firstOrNull()
                        cont.resume(location)
                    }
                }
                fusedLocationProviderClient.requestLocationUpdates(
                    locationRequest,
                    locationCallback,
                    Looper.getMainLooper()
                )

                cont.invokeOnCancellation {
                    fusedLocationProviderClient.removeLocationUpdates(locationCallback)
                    cont.resume(null)
                }
            }
        }
    }

}