package com.adyen.android.assignment

import androidx.lifecycle.SavedStateHandle
import com.adyen.android.assignment.domain.common.Result
import com.adyen.android.assignment.domain.model.Distance
import com.adyen.android.assignment.domain.model.Venue
import com.adyen.android.assignment.domain.usecase.GetLocationStatusUseCase
import com.adyen.android.assignment.domain.usecase.GetNearbyPlacesUseCase
import com.adyen.android.assignment.ui.NearbyPlacesViewModel
import com.adyen.android.assignment.ui.NearbyPlacesViewModel.Companion.VENUES_STATE
import com.adyen.android.assignment.ui.binding.NearbyPlacesUiEffect
import com.adyen.android.assignment.ui.binding.NearbyPlacesUiEvent
import com.adyen.android.assignment.ui.binding.NearbyPlacesUiState
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExperimentalCoroutinesApi
@ExtendWith(CoroutineScopeExtension::class)
class NearbyPlacesViewModelTest {

    private val savedStateHandle: SavedStateHandle = mock()
    private val getLocationStatusUseCase: GetLocationStatusUseCase = mock()
    private val getNearbyPlacesUseCase: GetNearbyPlacesUseCase = mock()


    @Test
    fun testInitialState() = runBlockingTest {
        val viewModel = getViewModel()
        val resultSet = mutableListOf<NearbyPlacesUiState>()
        val job = launch { viewModel.uiState.toList(resultSet) }

        assertEquals(NearbyPlacesUiState.Empty, resultSet.first())
        job.cancel()
    }

    @Test
    fun testInitialisationWithSavedState() = runBlockingTest {
        val venues = listOf(Venue("restaurant", Distance.Meters(1), "1234 AB"))
        whenever(savedStateHandle.contains(VENUES_STATE)).thenReturn(true)
        whenever(savedStateHandle.get<List<Venue>>(VENUES_STATE)).thenReturn(venues)
        val viewModel = getViewModel()

        val resultSet = mutableListOf<NearbyPlacesUiState>()
        val job = launch { viewModel.uiState.toList(resultSet) }

        assertEquals(NearbyPlacesUiState.Success(venues), resultSet.first())
        job.cancel()
    }

    @Test
    fun `Given location is disabled When OnLocationPermissionGranted Then should emit ShowLocationSettingsDialog`() =
        runBlockingTest {
            val viewModel = getViewModel()
            whenever(getLocationStatusUseCase.execute()).thenReturn(false)
            val resultSet = mutableListOf<NearbyPlacesUiEffect>()
            val job = launch { viewModel.uiEffects.toList(resultSet) }

            viewModel.dispatch(NearbyPlacesUiEvent.OnLocationPermissionGranted)

            assertEquals(NearbyPlacesUiEffect.ShowLocationSettingsDialog, resultSet.first())
            job.cancel()
        }

    @Test
    fun `Given location is enabled and use case fails When OnLocationPermissionGranted Then should emit Error`() =
        runBlockingTest {
            val viewModel = getViewModel()
            whenever(getLocationStatusUseCase.execute()).thenReturn(true)
            whenever(getNearbyPlacesUseCase.execute()).thenReturn(Result.Failed(mock()))
            val resultSet = mutableListOf<NearbyPlacesUiState>()

            viewModel.dispatch(NearbyPlacesUiEvent.OnLocationPermissionGranted)
            val job = launch { viewModel.uiState.toList(resultSet) }

            assertEquals(NearbyPlacesUiState.Error, resultSet.first())
            job.cancel()
        }

    @Test
    fun `Given location is enabled and use case returns empty When OnLocationPermissionGranted Then should emit Empty`() =
        runBlockingTest {
            val viewModel = getViewModel()
            whenever(getLocationStatusUseCase.execute()).thenReturn(true)
            whenever(getNearbyPlacesUseCase.execute()).thenReturn(Result.Success(emptyList()))
            val resultSet = mutableListOf<NearbyPlacesUiState>()

            viewModel.dispatch(NearbyPlacesUiEvent.OnLocationPermissionGranted)
            val job = launch { viewModel.uiState.toList(resultSet) }

            assertEquals(NearbyPlacesUiState.Empty, resultSet.first())
            job.cancel()
        }

    @Test
    fun `Given location is enabled and use case succeeds When OnLocationPermissionGranted Then should emit Success`() =
        runBlockingTest {
            val viewModel = getViewModel()
            val venues = listOf(Venue("restaurant", Distance.Meters(1), "1234 AB"))
            whenever(getLocationStatusUseCase.execute()).thenReturn(true)
            whenever(getNearbyPlacesUseCase.execute()).thenReturn(Result.Success(venues))
            val resultSet = mutableListOf<NearbyPlacesUiState>()

            viewModel.dispatch(NearbyPlacesUiEvent.OnLocationPermissionGranted)
            val job = launch { viewModel.uiState.toList(resultSet) }

            assertEquals(NearbyPlacesUiState.Success(venues), resultSet.first())
            job.cancel()
        }

    @Test
    fun `When OnLocationPermissionDenied Then should emit ShowLocationPermissionRationale`() =
        runBlockingTest {
            val viewModel = getViewModel()
            val resultSet = mutableListOf<NearbyPlacesUiEffect>()
            val job = launch { viewModel.uiEffects.toList(resultSet) }

            viewModel.dispatch(NearbyPlacesUiEvent.OnLocationPermissionDenied)

            assertEquals(NearbyPlacesUiEffect.ShowLocationPermissionRationale, resultSet.first())
            job.cancel()
        }

    @Test
    fun `When OnSearchButtonClicked Then should emit CheckLocationPermission`() =
        runBlockingTest {
            val viewModel = getViewModel()
            val resultSet = mutableListOf<NearbyPlacesUiEffect>()
            val job = launch { viewModel.uiEffects.toList(resultSet) }

            viewModel.dispatch(NearbyPlacesUiEvent.OnSearchButtonClicked)

            assertEquals(NearbyPlacesUiEffect.CheckLocationPermission, resultSet.first())
            job.cancel()
        }

    private fun getViewModel() = NearbyPlacesViewModel(
        savedStateHandle,
        getNearbyPlacesUseCase,
        getLocationStatusUseCase
    )
}