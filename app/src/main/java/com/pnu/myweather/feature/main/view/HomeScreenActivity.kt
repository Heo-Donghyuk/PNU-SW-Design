package com.pnu.myweather.feature.main.view

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.pnu.myweather.BuildConfig
import com.pnu.myweather.core.util.getLatestBaseDateTime
import com.pnu.myweather.core.weather.WeatherUiState
import com.pnu.myweather.feature.briefing.view.BriefingScreenActivity
import com.pnu.myweather.feature.component.Card
import com.pnu.myweather.feature.component.MyButton
import com.pnu.myweather.feature.developer.view.DeveloperScreenActivity
import com.pnu.myweather.feature.main.viewmodel.HomeViewModel
import com.pnu.myweather.feature.setting.view.SettingScreenActivity
import com.pnu.myweather.ui.theme.MyweatherTheme

class HomeScreenActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val homeViewModel: HomeViewModel = viewModel()
            MyweatherTheme {
                HomeScreen(
                    viewModel = homeViewModel,
                    onGoToBriefing = {
                        val weatherSummary = homeViewModel.weatherSummary.value
                        val intent = Intent(this, BriefingScreenActivity::class.java).apply {
                            putExtra("weatherSummary", weatherSummary)
                            // TODO: tomorrowSummary도 필요하면 같이 넣기
                        }
                        startActivity(intent)
                    },
                    onGoToSetting = {
                        startActivity(Intent(this, SettingScreenActivity::class.java))
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onGoToBriefing: () -> Unit,
    onGoToSetting: () -> Unit
) {
    val context = LocalContext.current
    val naverWeatherUrl by viewModel.naverWeatherUrl.collectAsState()
    val weatherState by viewModel.weatherState.collectAsState()
    val weatherSummary by viewModel.weatherSummary.collectAsState()
    val (baseDate, baseTime) = getLatestBaseDateTime()
    LaunchedEffect(Unit) {

        viewModel.fetchWeather(
            baseDate = baseDate,
            baseTime = baseTime,
            nx = 98, ny = 76       // ex) 장전동 (부산 금정구)
        )
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = {
                    SecretAppBarTitle("Weather") {
                        context.startActivity(Intent(context, DeveloperScreenActivity::class.java))
                    }
                },
                actions = {
                    IconButton(onClick = onGoToSetting) {
                        Icon(
                            imageVector = Icons.Filled.Settings,
                            contentDescription = "설정 화면으로 이동"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(30.dp)
        ) {
            Card {
                Column {
                    when (weatherState) {
                        is WeatherUiState.Loading -> {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 130.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator()
                            }
                        }

                        is WeatherUiState.Error -> {
                            Text(BuildConfig.WEATHER_API_KEY)
                            Text("에러 발생: ${(weatherState as WeatherUiState.Error).message}")
                        }

                        is WeatherUiState.Success -> {
                            WeatherCard(
                                weatherSummary = weatherSummary,
                                location = "임시위치",
                                fineDust = "임시",
                                ultraFineDust = "임시",
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.padding(top = 16.dp))
            MyButton(onClick = onGoToBriefing, enabled = weatherState is WeatherUiState.Success) {
                Text("브리핑")
            }
        }
    }
}