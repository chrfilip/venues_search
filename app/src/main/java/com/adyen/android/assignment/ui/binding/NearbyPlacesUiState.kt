package com.adyen.android.assignment.ui.binding

import com.adyen.android.assignment.domain.model.Venue

sealed class NearbyPlacesUiState {
    object Loading: NearbyPlacesUiState()
    object Empty: NearbyPlacesUiState()
    object Error: NearbyPlacesUiState()
    data class Success(val venues: List<Venue>): NearbyPlacesUiState()
}
