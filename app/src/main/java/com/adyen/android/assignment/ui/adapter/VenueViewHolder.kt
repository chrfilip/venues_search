package com.adyen.android.assignment.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.adyen.android.assignment.R
import com.adyen.android.assignment.databinding.ListItemVenueBinding
import com.adyen.android.assignment.domain.model.Distance
import com.adyen.android.assignment.domain.model.Venue

class VenueViewHolder private constructor(private val binding: ListItemVenueBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: Venue) = with(binding) {
        venueName.text = item.name
        venueAddress.text = item.address
        venueDistance.text = when (item.distance) {
            is Distance.KiloMeters -> itemView.resources.getString(R.string.distance_kilometers, String.format("%.2f", item.distance.value))
            is Distance.Meters -> itemView.resources.getString(R.string.distance_meters, item.distance.value.toString())
        }
    }

    companion object {
        fun from(parent: ViewGroup): VenueViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ListItemVenueBinding.inflate(layoutInflater, parent, false)
            return VenueViewHolder(binding)
        }
    }
}