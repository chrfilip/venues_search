package com.adyen.android.assignment.ui

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.DividerItemDecoration
import com.adyen.android.assignment.R
import com.adyen.android.assignment.databinding.NearbyPlacesFragmentBinding
import com.adyen.android.assignment.domain.model.Venue
import com.adyen.android.assignment.ui.adapter.VenuesListAdapter
import com.adyen.android.assignment.ui.binding.NearbyPlacesUiEffect
import com.adyen.android.assignment.ui.binding.NearbyPlacesUiEvent
import com.adyen.android.assignment.ui.binding.NearbyPlacesUiState
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class NearbyPlacesFragment : Fragment() {

    private val viewModel: NearbyPlacesViewModel by viewModels()

    private var _binding: NearbyPlacesFragmentBinding? = null
    private val binding get() = _binding!!

    private val adapter = VenuesListAdapter()

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                viewModel.dispatch(NearbyPlacesUiEvent.OnLocationPermissionGranted)
            } else {
                viewModel.dispatch(NearbyPlacesUiEvent.OnLocationPermissionDenied)
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = NearbyPlacesFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initUi()
        observeUiState()
        observeUiEffects()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initUi() {
        with(binding) {
            recyclerview.adapter = adapter
            ResourcesCompat.getDrawable(resources, R.drawable.divider, null)?.let {
                val decorator = DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
                decorator.setDrawable(it)
                recyclerview.addItemDecoration(decorator)
            }
            locationButton.setOnClickListener { viewModel.dispatch(NearbyPlacesUiEvent.OnSearchButtonClicked) }
        }
    }

    private fun observeUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    when (state) {
                        is NearbyPlacesUiState.Empty -> showEmptyState()
                        is NearbyPlacesUiState.Error -> showErrorState()
                        is NearbyPlacesUiState.Loading -> showLoadingState()
                        is NearbyPlacesUiState.Success -> showContentState(state.venues)
                    }
                }
            }
        }
    }

    private fun observeUiEffects() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiEffects.collect { effect ->
                    when (effect) {
                        NearbyPlacesUiEffect.ShowLocationPermissionRationale -> showLocationPermissionRationaleDialog()
                        NearbyPlacesUiEffect.ShowLocationSettingsDialog -> showLocationsSettingsDialog()
                        NearbyPlacesUiEffect.CheckLocationPermission -> checkLocationPermission()
                    }
                }
            }
        }
    }

    private fun showLoadingState() = showState(LayoutState.LOADING)

    private fun showEmptyState() {
        showState(LayoutState.EMPTY)
        binding.message.text = getString(R.string.empty_state_message)
    }

    private fun showErrorState() {
        showState(LayoutState.ERROR)
        binding.message.text = getString(R.string.generic_error_message)
    }

    private fun showContentState(venues: List<Venue>) {
        showState(LayoutState.CONTENT)
        adapter.submitList(venues)
    }

    private fun showState(layoutState: LayoutState) {
        with(binding) {
            progress.isVisible = layoutState == LayoutState.LOADING
            message.isVisible = layoutState == LayoutState.EMPTY || layoutState == LayoutState.ERROR
            recyclerview.isVisible = layoutState == LayoutState.CONTENT
        }
    }

    private fun checkLocationPermission() {
        when {
            isPermissionGranted() -> viewModel.dispatch(NearbyPlacesUiEvent.OnLocationPermissionGranted)
            else -> requestPermissionLauncher.launch(ACCESS_FINE_LOCATION)
        }
    }

    private fun isPermissionGranted() =
        ContextCompat.checkSelfPermission(
            requireContext(),
            ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

    private fun showLocationsSettingsDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.location_settings)
            .setMessage(R.string.enable_location_massage)
            .setPositiveButton(R.string.settings) { _, _ -> goToLocationSettings() }
            .setNegativeButton(android.R.string.cancel) { dialog, _ -> dialog.dismiss() }
            .show()
    }

    private fun showLocationPermissionRationaleDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.location_permission)
            .setMessage(R.string.location_permission_rationale)
            .setPositiveButton(R.string.settings) { _, _ -> goToAppSettings() }
            .setNegativeButton(android.R.string.cancel) { dialog, _ -> dialog.dismiss() }
            .show()
    }

    private fun goToLocationSettings() =
        startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))

    private fun goToAppSettings() {
        val intent = Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            Uri.fromParts("package", requireContext().packageName, this.javaClass.simpleName)
        )
        startActivity(intent)
    }

    enum class LayoutState {
        LOADING, ERROR, EMPTY, CONTENT
    }

    companion object {
        fun newInstance() = NearbyPlacesFragment()
    }
}