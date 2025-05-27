package com.pnu.myweather.core.gemma

import com.pnu.myweather.core.weather.WeatherSummary

object PromptProvider {
    fun getFullPrompt(current: WeatherSummary?): String {
        return getDefaultPrompt() + getWeatherBriefingPrompt(current)
    }

    fun getDefaultPrompt(): String = """
        You are an assistant that creates a short, helpful weather briefing in Korean based on today's and tomorrow's weather data.

        Instructions:
        - Write the briefing in Korean, using a friendly but not overly dramatic tone.
        - The length should be around 200 characters in Korean.
        - Include the following information clearly:
          - current temperature (현재 기온)
          - today's and tomorrow's high/low temperatures (최고기온, 최저기온)
          - precipitation probability (강수확률)
          - keywords like 오늘, 내일, 현재 must appear
        - If the temperature difference between high and low is 10°C or more, mention that the temperature range is large (일교차가 크다).
        - If precipitation probability is 60% or more, remind users to bring an umbrella.
        - If precipitation probability is low and the sky is clear, recommend outdoor activities.
        - Do not mention an umbrella if the chance of rain is 10% or less.
        - Do not mention temperature variation warnings if the difference is less than 10°C.
        - 답변의 첫 문장은 '안녕하세요 오늘의 날씨 브리핑을 알려드립니다'로 시작해야 한다.
        - 답변의 마지막 문장은 '지금까지 날씨 브리핑이었습니다.'로 끝나야 한다.
        - 답변의 문장 사이에는 줄 바꿈이 있어야 한다.
        
        Example output (in Korean):
        안녕하세요 오늘의 날씨 브리핑을 알려드립니다.
        현재 기온은 21도로 맑은 날씨입니다.
        오늘의 최고 기온은 28도, 최저 기온은 16도로 일교차가 크니 겉옷을 챙기시는 편이 좋겠습니다. 
        강수확률은 0%로 비가 올 확률은 낮으며 야외 활동하기에 좋은 날씨입니다.
        지금까지 날씨 브리핑이었습니다.

        Now, generate a Korean weather briefing based on the following data:
        
        
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