package com.adyen.android.assignment.ui

sealed class NearbyPlacesUiState {
    object Loading: NearbyPlacesUiState()
    data class Error(val message: String): NearbyPlacesUiState()
    object Success: NearbyPlacesUiState()
}
