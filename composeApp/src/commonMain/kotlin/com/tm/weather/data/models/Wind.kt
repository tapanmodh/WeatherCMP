package com.tm.weather.data.models

import kotlinx.serialization.Serializable

@Serializable
data class Wind(
    val deg: Int?,
    val gust: Double?,
    val speed: Double?
)