package com.pnu.myweather.core.util

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@SuppressLint("DefaultLocale")
@RequiresApi(Build.VERSION_CODES.O)
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