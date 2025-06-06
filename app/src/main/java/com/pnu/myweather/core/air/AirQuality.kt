// 파일: core/air/AirQuality.kt
package com.pnu.myweather.core.air

data class AirQuality(
    val pm10Value: String,   // 미세먼지(PM10) 농도 값, 문자열로 받아옴
    val pm25Value: String    // 초미세먼지(PM2.5) 농도 값
)