package com.pnu.myweather.core.util

import android.annotation.SuppressLint
import com.pnu.myweather.R
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@SuppressLint("DefaultLocale")
fun getLatestBaseDateTime(): Pair<String, String> {
    val now = LocalDateTime.now()
    val timeTable = listOf(2, 5, 8, 11, 14, 17, 20, 23)

    // 가장 가까운 이전 발표 시각 찾기
    val baseHour = timeTable.lastOrNull { it <= now.hour }
        ?: 23 // 0~1시는 전날 23시 예보 사용

    // 0시~1시라면 전날 날짜 사용
    val baseDate = if (now.hour < 2) {
        now.minusDays(1).format(DateTimeFormatter.ofPattern("yyyyMMdd"))
    } else {
        now.format(DateTimeFormatter.ofPattern("yyyyMMdd"))
    }

    val baseTime = String.format("%02d00", baseHour) // ex) "1400"

    return baseDate to baseTime
}

fun getWeatherDescription(sky: String, pty: String): String {
    return when (pty) {
        "0" -> { // 강수 없음 → 하늘 상태 기준
            when (sky) {
                "1" -> "맑음"
                "3" -> "구름 많음"
                "4" -> "흐림"
                else -> "알 수 없음"
            }
        }

        "1" -> "비"
        "2" -> "비 또는 눈"
        "3" -> "눈"
        "4" -> "소나기"
        else -> "강수 정보 없음"
    }
}

fun getWeatherIconRes(skyState: String?): Int {
    return when (skyState) {
        "맑음" -> R.drawable.clear
        "구름 많음" -> R.drawable.partly_cloudy
        "흐림" -> R.drawable.cloudy
        "비" -> R.drawable.rain
        "비 또는 눈" -> R.drawable.rain_or_snow
        "눈" -> R.drawable.snow
        "소나기" -> R.drawable.shower
        else -> R.drawable.cloudy // fallback 기본값
    }
}