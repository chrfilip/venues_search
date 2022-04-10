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
        val venues = listOf(
            venue1.copy(distance = 999, location = location.copy(address = "Somewhere", postcode = "1234 AB")),
            venue2.copy(distance = 1000, location = location.copy(address = "Somewhere", postcode = null)),
            venue3.copy(distance = 1001, location = location.copy(address = null, postcode = "1234 AB")),
            venue4.copy(distance = 1234, location = location.copy(address = null, postcode = null))
        )
        val response = ResponseWrapper(venues)

        val result = mapper.map(response)

        assertEquals(
            Venue(venues[0].name, Distance.Meters(999), "${venues[0].location.address}, ${venues[0].location.postcode}"),
            result[0]
        )
        assertEquals(
            Venue(venues[1].name, Distance.KiloMeters(1f), "${venues[1].location.address}"),
            result[1]
        )
        assertEquals(
            Venue(venues[2].name, Distance.KiloMeters(1.001f), "${venues[2].location.postcode}"),
            result[2]
        )
        assertEquals(
            Venue(venues[3].name, Distance.KiloMeters(1.234f), "Unknown address"),
            result[3]
        )
    }

}