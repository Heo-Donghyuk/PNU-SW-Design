package com.pnu.myweather.core.gemma

object PromptProvider {
    fun getDefaultPrompt(): String = """
        아래 날씨 데이터를 기반으로, 다음 조건에 따라 500자 내외의 짧고 친절한 날씨 브리핑을 만들어줘.
        1. 오늘 날씨에 대한 요약을 한 문장으로 제시해줘. (예: 비교적 서늘한 날씨입니다, 구름이 많은 날씨입니다 등)
        2. 날씨에 따라 시민들에게 도움이 될 만한 행동 조언을 한 문장으로 덧붙여줘. (예: 일교차가 크니 외투를 챙기세요, 자외선이 강하니 모자를 착용하세요 등)
    """.trimIndent()
}