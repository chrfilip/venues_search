package com.adyen.android.assignment.ui.binding

sealed class NearbyPlacesUiEvent {
    object OnLocationPermissionGranted: NearbyPlacesUiEvent()
    object OnLocationPermissionDenied: NearbyPlacesUiEvent()
    object OnSearchButtonClicked: NearbyPlacesUiEvent()
}
