package com.pnu.myweather.core.air


data class AirKoreaResponse(
    val response: ResponseBody
)

data class ResponseBody(
    val body: ResponseItems
)

data class ResponseItems(
    val items: List<AirQualityItem>
)

data class AirQualityItem(
    val dataTime: String,
    val stationName: String,
    val pm10Value: String?,
    val pm25Value: String?
)
