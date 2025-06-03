package com.pnu.myweather.feature.developer.view

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch


class DeveloperScreenActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DeveloperScreen()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeveloperScreen() {
    val activity = LocalActivity.current
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("🛠️ Developer Options") }, navigationIcon = {
                IconButton(onClick = { activity?.finish() }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                }
            })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Top
        ) {
            Button(
                onClick = {
                    context.startActivity(Intent(context, PromptEditActivity::class.java))
                }
            ) {
                Text("기본 프롬프트 수정")
            }

            // 앞으로 여기 다른 개발자용 기능 버튼들도 추가하면 돼!
            Spacer(modifier = Modifier.padding(vertical = 12.dp))

            Button(
                onClick = {
                    // 미세먼지 API 호출
                    val apiKey = "GyUs3LP7spCIM1xktKtSWLnFh3cEn%2FBdohl5oeZhTu6NmPVkrhQ22hXRoGs1%2BMzGnhi7lB0O1MNBzAb3WkgiSw%3D%3D"
                    kotlinx.coroutines.CoroutineScope(kotlinx.coroutines.Dispatchers.IO).launch {
                        try {
                            val response = com.pnu.myweather.core.air.AirKoreaClient.apiService.getAirQuality(
                                stationName = "종로구",
                                serviceKey = apiKey
                            )
                            val item = response.response.body.items.firstOrNull()
                            item?.let {
                                android.util.Log.d("AirQuality", "종로구 - PM10: ${it.pm10Value}, PM2.5: ${it.pm25Value}")
                            }
                        } catch (e: Exception) {
                            android.util.Log.e("AirQuality", "API 호출 실패", e)
                        }
                    }
                }
            ) {
                Text("미세먼지 API 호출 테스트")
            }
        }
    }
}