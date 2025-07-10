package com.tm.weather.data.models

import kotlinx.serialization.Serializable

@Serializable
data class WeatherResponse(
    val main: Main? = null,
    val name: String? = null,
    val visibility: Int? = null,
    val weather: List<Weather>? = null,
    val wind: Wind? = null
)