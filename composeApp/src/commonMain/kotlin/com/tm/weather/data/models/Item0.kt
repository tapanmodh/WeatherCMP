package com.tm.weather.data.models

import kotlinx.serialization.Serializable

@Serializable
data class Item0(
    val clouds: Clouds?=null,
    val dt: Int,
    val dt_txt: String,
    val main: Main,
    val pop: Double?= null,
    val rain: Rain?= null,
    val sys: Sys?= null,
    val visibility: Int,
    val weather: List<Weather>,
    val wind: Wind
)