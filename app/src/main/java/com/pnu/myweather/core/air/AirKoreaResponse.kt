package com.pnu.myweather.core.air

data class AirKoreaResponse(
    val response: ResponseWrapper
)

data class ResponseWrapper(
    val header: Header,
    val body: ResponseBody
)

data class Header(
    val resultCode: String,
    val resultMsg: String
)

data class ResponseBody(
    val totalCount: Int,
    val items: List<AirQualityItem>,
    val pageNo: Int,
    val numOfRows: Int
)

data class AirQualityItem(
    val dataTime: String,
    val pm10Value: String?,
    val pm25Value: String?
)