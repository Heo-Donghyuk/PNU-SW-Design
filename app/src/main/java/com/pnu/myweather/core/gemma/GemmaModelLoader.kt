package com.pnu.myweather.core.gemma

import android.app.Application
import com.google.mediapipe.tasks.genai.llminference.LlmInference
import com.google.mediapipe.tasks.genai.llminference.LlmInferenceSession
import java.io.File

object GemmaModelLoader {
    suspend fun loadModel(application: Application, assetFileName: String): LlmInferenceSession {
        val destinationFile = File(application.filesDir, assetFileName)
        if (!destinationFile.exists()) {
            application.assets.open(assetFileName).use { input ->
                destinationFile.outputStream().use { output -> input.copyTo(output) }
            }
        }

        val interfaceOptions = LlmInference.LlmInferenceOptions.builder()
            .setModelPath(destinationFile.absolutePath)
            .setMaxTokens(2000)
            .setPreferredBackend(LlmInference.Backend.CPU)
            .build()

        val llmInference = LlmInference.createFromOptions(application, interfaceOptions)

        val sessionOptions = LlmInferenceSession.LlmInferenceSessionOptions.builder()
            .setTemperature(0.8f)
            .setTopK(40)
            .setTopP(0.95f)
            .build()

        return LlmInferenceSession.createFromOptions(llmInference, sessionOptions)
    }
}