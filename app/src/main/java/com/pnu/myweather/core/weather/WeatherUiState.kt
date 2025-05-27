package com.pnu.myweather.core.weather

sealed class WeatherUiState {
    object Loading : WeatherUiState()
    data class Success(
        val groupedForecasts: Map<Pair<String, String>, List<ForecastItem>>
    ) : WeatherUiState()

    data class Error(val message: String) : WeatherUiState()
}