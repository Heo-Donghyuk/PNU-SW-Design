package com.pnu.myweather.core.gemma

import android.content.Context
import com.pnu.myweather.core.weather.WeatherSummary
import java.io.File

object PromptProvider {
    fun getFullPrompt(context: Context, current: WeatherSummary?): String {
        return loadPrompt(context) + getWeatherBriefingPrompt(current)
    }

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

    private const val FILE_NAME = "editable_prompt.txt"

    fun loadPrompt(context: Context): String {
        val file = File(context.filesDir, FILE_NAME)

        if (!file.exists()) {
            // 기본 프롬프트로 초기화
            val defaultPrompt = context.assets.open("prompt/default_prompt.txt")
                .bufferedReader().use { it.readText() }
            file.writeText(defaultPrompt)
            return defaultPrompt
        }

        return file.readText()
    }

    fun savePrompt(context: Context, prompt: String) {
        File(context.filesDir, FILE_NAME).writeText(prompt)
    }

    fun resetPrompt(context: Context) {
        val defaultPrompt = context.assets.open("prompt/default_prompt.txt")
            .bufferedReader().use { it.readText() }
        File(context.filesDir, FILE_NAME).writeText(defaultPrompt)
    }
}