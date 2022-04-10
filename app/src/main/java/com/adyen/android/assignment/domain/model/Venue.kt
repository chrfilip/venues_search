package com.adyen.android.assignment.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Venue(
    val name: String,
    val distance: Int,
    val address: String
) : Parcelable
