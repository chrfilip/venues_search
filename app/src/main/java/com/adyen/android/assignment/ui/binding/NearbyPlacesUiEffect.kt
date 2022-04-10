package com.adyen.android.assignment.ui.binding

sealed class NearbyPlacesUiEffect {
    object ShowLocationSettingsDialog: NearbyPlacesUiEffect()
    object ShowLocationPermissionRationale: NearbyPlacesUiEffect()
    object CheckLocationPermission: NearbyPlacesUiEffect()
}
