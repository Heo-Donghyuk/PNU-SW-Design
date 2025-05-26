package com.pnu.myweather.feature.main.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pnu.myweather.BuildConfig
import com.pnu.myweather.core.weather.ForecastItem
import com.pnu.myweather.core.weather.WeatherApiClient
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
                val items: List<ForecastItem> = response.response.body.items.item
                _weatherState.value = WeatherUiState.Success(items)
            } catch (e: Exception) {
                _weatherState.value = WeatherUiState.Error(e.message ?: "알 수 없는 에러")
            }
        }
    }
}