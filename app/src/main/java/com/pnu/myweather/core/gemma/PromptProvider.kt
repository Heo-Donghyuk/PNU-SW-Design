package com.pnu.myweather.core.gemma

import com.pnu.myweather.core.weather.WeatherSummary

object PromptProvider {
    fun getFullPrompt(current: WeatherSummary?): String {
        return getDefaultPrompt() + getWeatherBriefingPrompt(current)
    }

    fun getDefaultPrompt(): String = """
        아래 날씨 데이터를 기반으로, 다음 조건에 따라 400자 내외의 짧고 친절한 날씨 브리핑을 만들어줘.
        최저, 최고 기온이 10도 이상 차이나면 일교차가 크다고 알려줘
        만약 강수확률이 60% 이상이면 우산을 챙기라는 말을 덧붙여 줘
        
    """.trimIndent()

    fun getWeatherBriefingPrompt(current: WeatherSummary?): String {
        fun WeatherSummary?.describe(prefix: String): String {
            if (this == null) return "$prefix 날씨 정보를 불러오지 못했습니다."
            return buildString {
                appendLine("$prefix 날씨입니다.")
                temperature?.let { appendLine("현재 기온은 ${it}입니다.") }
                skyState?.let { appendLine("하늘 상태는 ${it}입니다.") }
                minTemp?.let { appendLine("최저 기온은 ${it}입니다.") }
                maxTemp?.let { appendLine("최고 기온은 ${it}입니다.") }
                humidity?.let { appendLine("습도는 ${it}입니다.") }
                precipitation?.let { appendLine("강수 확률은 ${it}입니다.") }
            }
        }

        return listOf(
            current.describe("현재"),
        ).joinToString("\n\n")
    }
}