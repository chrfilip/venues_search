package com.adyen.android.assignment.ui

import androidx.lifecycle.ViewModel
import com.adyen.android.assignment.domain.usecase.GetNearbyPlacesUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class VenueDetailsViewModel(
    private val getNearbyPlacesUseCase: GetNearbyPlacesUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow<NearbyPlacesUiState>(NearbyPlacesUiState.Loading)
    val uiState: StateFlow<NearbyPlacesUiState> = _uiState

    fun dispatch(event: NearbyPlacesUiEvent) {
        when (event) {

        }
    }

    private fun handleSuccess() {
    }

    private fun handleFailure() {
    }

}
