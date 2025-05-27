package com.pnu.myweather.core.weather

data class WeatherSummary(
    val temperature: String?,
    val skyState: String?,
    val minTemp: String?,
    val maxTemp: String?,
    val humidity: String?,
    val precipitation: String?
)