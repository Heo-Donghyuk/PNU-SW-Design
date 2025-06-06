// 파일: feature/main/viewmodel/HomeViewModel.kt
package com.pnu.myweather.feature.main.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pnu.myweather.BuildConfig
import com.pnu.myweather.core.air.AirKoreaClient
import com.pnu.myweather.core.air.AirQuality
import com.pnu.myweather.core.util.getWeatherDescription
import com.pnu.myweather.core.weather.ForecastItem
import com.pnu.myweather.core.weather.WeatherApiClient
import com.pnu.myweather.core.weather.WeatherSummary
import com.pnu.myweather.core.weather.WeatherUiState
import com.pnu.myweather.feature.setting.model.LocationState
import com.pnu.myweather.feature.setting.view.LocationPreference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeViewModel : ViewModel() {
    private val _locationState = MutableStateFlow(LocationState())
    val locationState: StateFlow<LocationState> = _locationState

    private val _naverWeatherUrl = MutableStateFlow("")
    val naverWeatherUrl: StateFlow<String> = _naverWeatherUrl

    private val _weatherState = MutableStateFlow<WeatherUiState>(WeatherUiState.Loading)
    val weatherState: StateFlow<WeatherUiState> = _weatherState

    private val _weatherSummary = MutableStateFlow<WeatherSummary?>(null)
    val weatherSummary: StateFlow<WeatherSummary?> = _weatherSummary

    private val _airQualityState = MutableStateFlow<AirQuality?>(null)
    val airQualityState: StateFlow<AirQuality?> = _airQualityState

    fun loadLocation(context: Context) {
        val sido = LocationPreference.getSido(context)
        val gu = LocationPreference.getGu(context)
        val dong = LocationPreference.getDong(context)
        val nx = LocationPreference.getNx(context)
        val ny = LocationPreference.getNy(context)
        val station = LocationPreference.getStation(context)

        val newLoc = LocationState(
            sido = sido,
            gu = gu,
            dong = dong,
            nx = nx,
            ny = ny,
            station = station
        )

        _locationState.value = newLoc

        if (sido.isNotBlank() && gu.isNotBlank()) {
            _naverWeatherUrl.value =
                "https://m.search.naver.com/search.naver?query=${sido} ${gu} ${dong} 날씨"
        } else {
            _naverWeatherUrl.value = ""
        }
    }


    fun fetchWeather(context: Context, baseDate: String, baseTime: String) {
        loadLocation(context)
        val loc = _locationState.value

        if (loc.nx < 0 || loc.ny < 0) {
            _weatherState.value = WeatherUiState.Error("위치 정보가 없습니다.")
            return
        }

        viewModelScope.launch {
            _weatherState.value = WeatherUiState.Loading
            try {
                val response = WeatherApiClient.service.getForecast(
                    serviceKey = BuildConfig.WEATHER_API_KEY,
                    baseDate = baseDate,
                    baseTime = baseTime,
                    nx = loc.nx,
                    ny = loc.ny,
                    dataType = "JSON"
                )
                val items = response.response.body.items.item
                processWeatherData(items)
                fetchAirQuality(context) //날씨 호출이 성공했으면, 바로 미세먼지 API도 호출
            } catch (e: Exception) {
                _weatherState.value = WeatherUiState.Error(e.message ?: "알 수 없는 에러")
            }
        }
    }

    private fun processWeatherData(items: List<ForecastItem>) {
        val requiredCategories = setOf("TMP", "SKY", "PTY", "REH", "POP")
        val grouped = items.groupBy { it.fcstDate to it.fcstTime }
        val filteredGrouped = grouped.filterValues { forecasts ->
            val present = forecasts.map { it.category }.toSet()
            requiredCategories.all { it in present }
        }
        val earliestEntry = filteredGrouped.entries.minByOrNull { (dateTime, _) ->
            val (date, time) = dateTime
            "$date$time"
        }
        val earliest = earliestEntry?.value ?: emptyList()

        fun findValue(category: String, from: List<ForecastItem>): String? =
            from.find { it.category == category }?.fcstValue

        val currentTemp = findValue("TMP", earliest)?.let { "$it°" }
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

        val minTemp = temperatureValues.minOrNull()?.let { "${it}°" } ?: "정보 없음"
        val maxTemp = temperatureValues.maxOrNull()?.let { "${it}°" } ?: "정보 없음"

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

    private fun fetchAirQuality(context: Context) {
        loadLocation(context)
        val stationName = _locationState.value.station.ifBlank { return }

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val serviceKey =
                    "GyUs3LP7spCIM1xktKtSWLnFh3cEn/Bdohl5oeZhTu6NmPVkrhQ22hXRoGs1+MzGnhi7lB0O1MNBzAb3WkgiSw=="
                val response = AirKoreaClient.apiService.getAirQuality(
                    stationName = stationName,
                    serviceKey = serviceKey
                )
                val item = response.response.body.items.firstOrNull()
                if (item != null) {
                    val pm10Int = item.pm10Value?.toIntOrNull() ?: -1
                    val pm25Int = item.pm25Value?.toIntOrNull() ?: -1

                    val pm10Grade = getAirGrade(pm10Int, isPM25 = false)
                    val pm25Grade = getAirGrade(pm25Int, isPM25 = true)

                    val airQuality = AirQuality(
                        pm10Value = pm10Grade,
                        pm25Value = pm25Grade,
                    )
                    withContext(Dispatchers.Main) {
                        _airQualityState.value = airQuality
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _airQualityState.value = null
                }
            }
        }
    }

    private fun getAirGrade(value: Int, isPM25: Boolean): String {
        return if (isPM25) {
            when (value) {
                in 0..15 -> "좋음"
                in 16..35 -> "보통"
                in 36..75 -> "나쁨"
                else -> "매우 나쁨"
            }
        } else {
            when (value) {
                in 0..30 -> "좋음"
                in 31..80 -> "보통"
                in 81..150 -> "나쁨"
                else -> "매우 나쁨"
            }
        }
    }
}