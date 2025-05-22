package com.pnu.myweather.feature.briefing.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pnu.myweather.core.gemma.GemmaModelLoader
import com.pnu.myweather.core.gemma.GemmaSessionManager
import com.pnu.myweather.core.gemma.GemmaState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class BreifingViewModel(
    application: Application,
) : ViewModel() {
    private val _gemmaState: MutableStateFlow<GemmaState> = MutableStateFlow(GemmaState.Idle)
    val gemmaState: StateFlow<GemmaState> = _gemmaState

    init {
        viewModelScope.launch {
            _gemmaState.value = GemmaState.Loading
            try {
                val session = GemmaModelLoader.loadModel(application, "gemma3-1B-it-int4.task")
                _gemmaState.value = GemmaState.Ready(GemmaSessionManager(session))
            } catch (e: Exception) {
                Log.e("BreifingViewModel", "Model load error ${e.message}", e)
                _gemmaState.value = GemmaState.Error
            }
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
