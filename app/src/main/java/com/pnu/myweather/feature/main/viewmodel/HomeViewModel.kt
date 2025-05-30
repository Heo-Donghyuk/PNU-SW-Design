package com.pnu.myweather.feature.main.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pnu.myweather.BuildConfig
import com.pnu.myweather.core.util.getWeatherDescription
import com.pnu.myweather.core.weather.ForecastItem
import com.pnu.myweather.core.weather.WeatherApiClient
import com.pnu.myweather.core.weather.WeatherSummary
import com.pnu.myweather.core.weather.WeatherUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    private val _naverWeatherUrl =
        MutableStateFlow("https://m.search.naver.com/search.naver?query=부산광역시 금정구 날씨")
    val naverWeatherUrl: StateFlow<String> = _naverWeatherUrl

    fun updateMessage(newUrl: String) {
        _naverWeatherUrl.value = newUrl
    }

    private val _weatherState = MutableStateFlow<WeatherUiState>(WeatherUiState.Loading)
    val weatherState: StateFlow<WeatherUiState> = _weatherState

    private val _weatherSummary = MutableStateFlow<WeatherSummary?>(null)
    val weatherSummary: StateFlow<WeatherSummary?> = _weatherSummary

    fun fetchWeather(
        baseDate: String,
        baseTime: String,
        nx: Int,
        ny: Int
    ) {
        viewModelScope.launch {
            _weatherState.value = WeatherUiState.Loading
            try {
                val response = WeatherApiClient.service.getForecast(
                    serviceKey = BuildConfig.WEATHER_API_KEY,
                    baseDate = baseDate,
                    baseTime = baseTime,
                    nx = nx,
                    ny = ny,
                    dataType = "JSON"
                )
                val items = response.response.body.items.item
                processWeatherData(response.response.body.items.item)
            } catch (e: Exception) {
                _weatherState.value = WeatherUiState.Error(e.message ?: "알 수 없는 에러")
            }
        }
    }

    private fun processWeatherData(items: List<ForecastItem>) {
        val requiredCategories = setOf("TMP", "SKY", "PTY", "REH", "POP")
        val grouped = items.groupBy { it.fcstDate to it.fcstTime }

        val filteredGrouped = grouped.filterValues { forecasts ->
            val presentCategories = forecasts.map { it.category }.toSet()
            requiredCategories.all { it in presentCategories }
        }

        val earliestEntry = filteredGrouped.entries.minByOrNull { (dateTime, _) ->
            val (date, time) = dateTime
            "$date$time"
        }
        val earliest = earliestEntry?.value ?: emptyList()

        fun findValue(category: String, from: List<ForecastItem>): String? =
            from.find { it.category == category }?.fcstValue

        val currentTemp = findValue("TMP", earliest)?.let { "$it°C" }
        val currentSky = findValue("SKY", earliest)
        val currentPty = findValue("PTY", earliest)
        val currentWeather = if (currentSky != null && currentPty != null) {
            getWeatherDescription(currentSky, currentPty)
        } else null
        val humidity = findValue("REH", earliest)?.let { "$it%" }
        val pop = findValue("POP", earliest)?.let { "$it%" }

        val temperatureValues = items
            .filter { it.category == "TMP" }
            .mapNotNull { it.fcstValue.toFloatOrNull() }

        val minTemp = temperatureValues.minOrNull()?.let { "${it}°C" } ?: "정보 없음"
        val maxTemp = temperatureValues.maxOrNull()?.let { "${it}°C" } ?: "정보 없음"

        _weatherSummary.value = WeatherSummary(
            temperature = currentTemp,
            skyState = currentWeather,
            minTemp = minTemp,
            maxTemp = maxTemp,
            humidity = humidity,
            precipitation = pop
        )

        _weatherState.value = WeatherUiState.Success(filteredGrouped)
    }
}