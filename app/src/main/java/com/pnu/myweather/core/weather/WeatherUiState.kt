package com.pnu.myweather.core.weather

sealed class WeatherUiState {
    object Loading : WeatherUiState()
    data class Success(val items: List<ForecastItem>) : WeatherUiState()
    data class Error(val message: String) : WeatherUiState()
}