package com.adyen.android.assignment

import com.adyen.android.assignment.data.api.model.*
import com.nhaarman.mockitokotlin2.mock

var location = Location(
    address = "",
    country = "",
    locality = "",
    neighbourhood = emptyList(),
    postcode = "",
    region = ""
)

val restaurantCategory = Category(
    icon = mock(),
    id = "18075jhv",
    name = "restaurant"
)

val parkCategory = Category(
    icon = mock(),
    id = "34908tbh",
    name = "park"
)

val venue1 = Result(
    name = "domino's pizza",
    location = location.copy(address = "Some street 245"),
    categories = listOf(restaurantCategory),
    distance = 6,
    geocode = mock(),
    timezone = ""
)

val venue2 = Result(
    name = "new york pizza",
    location = location.copy(address = "Somewhere 33"),
    categories = listOf(restaurantCategory),
    distance = 5,
    geocode = mock(),
    timezone = ""
)

val venue3 = Result(
    name = "Amstelpark",
    location = location.copy(address = "Some street 465"),
    categories = listOf(parkCategory),
    distance = 7,
    geocode = mock(),
    timezone = ""
)

val venue4 = Result(
    name = "Vondelpark",
    location = location.copy(address = "straat 347"),
    categories = listOf(parkCategory),
    distance = 8,
    geocode = mock(),
    timezone = ""
)

val venues = listOf(venue1, venue2, venue3, venue4)

val venueSearchSuccessResponse = ResponseWrapper(
    results = venues
)
