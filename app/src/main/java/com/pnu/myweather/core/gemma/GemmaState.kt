package com.pnu.myweather.core.gemma

sealed interface GemmaState {
    data object Idle : GemmaState
    data object Loading : GemmaState
    data class Ready(val sessionManager: GemmaSessionManager) : GemmaState
    data object Error : GemmaState
}