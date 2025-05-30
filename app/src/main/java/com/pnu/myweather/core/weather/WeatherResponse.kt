package com.pnu.myweather.core.weather

data class WeatherResponse(
    val response: ResponseData
)

data class ResponseData(
    val body: BodyData
)

data class BodyData(
    val items: ItemsData
)

data class ItemsData(
    val item: List<ForecastItem>
)

data class ForecastItem(
    val baseDate: String,
    val baseTime: String,
    val category: String,
    val fcstDate: String,
    val fcstTime: String,
    val fcstValue: String,
    val nx: Int,
    val ny: Int
)