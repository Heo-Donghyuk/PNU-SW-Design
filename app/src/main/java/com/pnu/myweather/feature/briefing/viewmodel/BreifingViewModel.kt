package com.pnu.myweather.feature.briefing.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.ViewModel
import com.pnu.myweather.core.gemma.GemmaSessionHolder
import com.pnu.myweather.core.gemma.GemmaState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class BreifingViewModel(
    application: Application,
) : ViewModel() {
    private val _gemmaState: MutableStateFlow<GemmaState> = MutableStateFlow(GemmaState.Idle)
    val gemmaState: StateFlow<GemmaState> = _gemmaState

    init {
        val sessionManager = GemmaSessionHolder.sessionManager
        if (sessionManager != null) {
            _gemmaState.value = GemmaState.Ready(sessionManager)
        } else {
            _gemmaState.value = GemmaState.Error
            Log.e("BreifingViewModel", "SessionManager was null! Did you forget to preload it?")
        }
    }

    fun sendQuery(inputPrompt: String) {
        val state = _gemmaState.value
        if (state is GemmaState.Ready) {
            state.sessionManager.sendQuery(inputPrompt)
        } else {
            Log.e("BreifingViewModel", "Cannot send query: model not ready")
        }
    }
}
