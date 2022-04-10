package com.adyen.android.assignment

import com.adyen.android.assignment.data.api.model.ResponseWrapper
import com.adyen.android.assignment.domain.model.Distance
import com.adyen.android.assignment.domain.model.Mapper
import com.adyen.android.assignment.domain.model.Venue
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class MapperTest {

    private val mapper = Mapper()

    @Test
    fun testMap() {
        val response = ResponseWrapper(
            results = listOf(
                venue1.copy(distance = 999),
                venue2.copy(distance = 1000)
            )
        )

        val result = mapper.map(response)

        assertEquals(
            Venue(venue1.name, Distance.Meters(999), "${venue1.location.address}, ${venue1.location.postcode}"),
            result[0]
        )
        assertEquals(
            Venue(venue2.name, Distance.KiloMeters(1f), "${venue2.location.address}, ${venue2.location.postcode}"),
            result[1]
        )
    }

}