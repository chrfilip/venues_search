package com.adyen.android.assignment.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adyen.android.assignment.domain.common.Result
import com.adyen.android.assignment.domain.model.Venue
import com.adyen.android.assignment.domain.usecase.GetLocationStatusUseCase
import com.adyen.android.assignment.domain.usecase.GetNearbyPlacesUseCase
import com.adyen.android.assignment.ui.binding.NearbyPlacesUiEffect
import com.adyen.android.assignment.ui.binding.NearbyPlacesUiEvent
import com.adyen.android.assignment.ui.binding.NearbyPlacesUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NearbyPlacesViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getNearbyPlacesUseCase: GetNearbyPlacesUseCase,
    private val getLocationStatusUseCase: GetLocationStatusUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<NearbyPlacesUiState>(NearbyPlacesUiState.Empty)
    val uiState: StateFlow<NearbyPlacesUiState> = _uiState

    private val _uiEffects = MutableSharedFlow<NearbyPlacesUiEffect>(0)
    val uiEffects: SharedFlow<NearbyPlacesUiEffect> = _uiEffects

    private var loadJob: Job? = null

    private var venues: List<Venue> = savedStateHandle[VENUES_STATE] ?: emptyList()
        set(value) {
            savedStateHandle[VENUES_STATE] = value
            field = value
        }

    init {
        if (savedStateHandle.contains(VENUES_STATE)) {
            handleSuccess(venues)
        }
    }

    fun dispatch(event: NearbyPlacesUiEvent) {
        when (event) {
            NearbyPlacesUiEvent.OnLocationPermissionGranted -> checkLocationSettings()
            NearbyPlacesUiEvent.OnLocationPermissionDenied -> showLocationPermissionRationale()
            NearbyPlacesUiEvent.OnSearchButtonClicked -> checkLocationPermission()
        }
    }

    private fun checkLocationPermission() {
        viewModelScope.launch {
            _uiEffects.emit(NearbyPlacesUiEffect.CheckLocationPermission)
        }
    }

    private fun checkLocationSettings() {
        if (getLocationStatusUseCase.execute()) {
            loadData()
        } else {
            showLocationSettingsDialog()
        }
    }

    private fun loadData() {
        _uiState.value = NearbyPlacesUiState.Loading
        loadJob?.cancel()
        loadJob = viewModelScope.launch {
            when (val result = getNearbyPlacesUseCase.execute()) {
                is Result.Failed -> handleFailure()
                is Result.Success -> handleSuccess(result.data)
            }
        }
    }

    private fun handleSuccess(venues: List<Venue>) {
        _uiState.value = if (venues.isEmpty()) {
            NearbyPlacesUiState.Empty
        } else {
            this.venues = venues
            NearbyPlacesUiState.Success(venues)
        }
    }

    private fun handleFailure() {
        _uiState.value = NearbyPlacesUiState.Error
    }

    private fun showLocationPermissionRationale() {
        viewModelScope.launch {
            _uiEffects.emit(NearbyPlacesUiEffect.ShowLocationPermissionRationale)
        }
    }

    private fun showLocationSettingsDialog() {
        viewModelScope.launch {
            _uiEffects.emit(NearbyPlacesUiEffect.ShowLocationSettingsDialog)
        }
    }

    override fun onCleared() {
        super.onCleared()
        loadJob?.cancel()
    }

    companion object {
        const val VENUES_STATE = "VENUES_STATE"
    }
}
