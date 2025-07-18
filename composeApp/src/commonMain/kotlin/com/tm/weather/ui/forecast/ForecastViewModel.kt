package com.tm.weather.ui.forecast

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tm.weather.data.models.ForecastResponse
import com.tm.weather.data.models.Item0
import com.tm.weather.data.repository.WeatherRepository
import dev.icerock.moko.geo.LatLng
import dev.icerock.moko.geo.LocationTracker
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class ForecastViewModel(val locationTracker: LocationTracker) : ViewModel() {

    val repository = WeatherRepository()

    private val _state = MutableStateFlow<ForecastState>(ForecastState.Loading)
    val state = _state.asStateFlow()

    fun getForecast() {
        viewModelScope.launch {
            _state.value = ForecastState.Loading
            val location = getCurrentLocation()
            try {
                val response = repository.fetchForecast(location)
                val dailyData = getDailyForecast(response)
                val weeklyData = getWeeklyForecast(response).map { it.value.first() }
                _state.value = ForecastState.Data(dailyData, weeklyData)
            } catch (e: Exception) {
                // Handle error
                e.printStackTrace()
                _state.value = ForecastState.Error(e)
            }
        }
    }

    private fun getDailyForecast(response: ForecastResponse): List<Item0> {
        val sortedData = response.list.sortedBy { it.dt }.map { it.dt_txt.split(" ")[0] }
        val groupedData = response.list.groupBy { it.dt_txt.split(" ")[0] }
        return groupedData[sortedData[0]] ?: emptyList()
    }

    private fun getWeeklyForecast(response: ForecastResponse): Map<String, List<Item0>> {
        val groupedData = response.list.sortedBy { it.dt }.groupBy { it.dt_txt.split(" ")[0] }
        return groupedData
    }

    private suspend fun getCurrentLocation() : LatLng {
        locationTracker.startTracking()
        val location = locationTracker.getLocationsFlow().first()
        locationTracker.stopTracking()
        return location
    }
}

sealed class ForecastState {
    object Loading : ForecastState()
    data class Data(val dailyData: List<Item0>, val weeklyData: List<Item0>) : ForecastState()
    data class Error(val message: Throwable) : ForecastState()
}