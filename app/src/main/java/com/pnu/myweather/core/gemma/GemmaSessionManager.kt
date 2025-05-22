package com.pnu.myweather.core.gemma

import com.google.mediapipe.tasks.genai.llminference.LlmInferenceSession
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class GemmaSessionManager(
    private val session: LlmInferenceSession
) {
    private val _response = MutableStateFlow("")
    val response: StateFlow<String> = _response

    private val _responding = MutableStateFlow(false)
    val responding: StateFlow<Boolean> = _responding

    fun sendQuery(input: String) {
        _response.value = ""
        _responding.value = true

        session.addQueryChunk(input)
        session.generateResponseAsync { partial, done ->
            _response.value += partial
            if (done) _responding.value = false
        }
    }
}