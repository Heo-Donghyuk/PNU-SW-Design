package com.pnu.myweather.feature.setting.model

data class LocationState(
    val sido: String = "",
    val gu: String = "",
    val dong: String = "",
    val nx: Int = -1,
    val ny: Int = -1,
    val station: String = ""
) {
    val locationString: String
        get() = listOf(sido, gu, dong)
            .filter { it.isNotBlank() }
            .joinToString(" ")
}