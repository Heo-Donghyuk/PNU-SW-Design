package com.pnu.myweather

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.lifecycle.lifecycleScope
import com.pnu.myweather.core.gemma.GemmaModelLoader
import com.pnu.myweather.core.gemma.GemmaSessionHolder
import com.pnu.myweather.core.gemma.GemmaSessionManager
import com.pnu.myweather.feature.main.view.HomeScreenActivity
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            try { // 앱 시작 시 머신러닝 모델 preload
                if (GemmaSessionHolder.sessionManager == null) {
                    val session = GemmaModelLoader.loadModel(application, "gemma3-1B-it-int4.task")
                    GemmaSessionHolder.sessionManager = GemmaSessionManager(session)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

            val intent = Intent(this@MainActivity, HomeScreenActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}