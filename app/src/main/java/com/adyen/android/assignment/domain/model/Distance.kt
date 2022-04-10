package com.adyen.android.assignment.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

sealed class Distance: Parcelable {
    @Parcelize
    data class Meters(val value: Int): Distance()
    @Parcelize
    data class KiloMeters(val value: Float): Distance()
}
