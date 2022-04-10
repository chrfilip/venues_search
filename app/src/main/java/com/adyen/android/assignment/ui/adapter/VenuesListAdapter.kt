package com.adyen.android.assignment.ui.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.adyen.android.assignment.domain.model.Venue

class VenuesListAdapter : ListAdapter<Venue, VenueViewHolder>(DiffCallBack()) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): VenueViewHolder {
        return VenueViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: VenueViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    private class DiffCallBack : DiffUtil.ItemCallback<Venue>() {

        override fun areItemsTheSame(oldItem: Venue, newItem: Venue): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: Venue, newItem: Venue): Boolean {
            return oldItem == newItem
        }

    }
}