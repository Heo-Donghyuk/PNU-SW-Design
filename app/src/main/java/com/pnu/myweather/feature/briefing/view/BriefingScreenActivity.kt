package com.pnu.myweather.feature.briefing.view

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.pnu.myweather.core.gemma.GemmaState
import com.pnu.myweather.core.gemma.PromptProvider
import com.pnu.myweather.core.util.ExternalAppUtils
import com.pnu.myweather.core.weather.WeatherSummary
import com.pnu.myweather.feature.briefing.viewmodel.BreifingViewModel

class BriefingScreenActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModel = ViewModelProvider(
            this,
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return BreifingViewModel(application) as T
                }
            }
        ).get(BreifingViewModel::class.java)

        val weatherSummary = intent.getParcelableExtra<WeatherSummary>("weatherSummary")

        setContent {
            BriefingScreen(
                viewModel = viewModel(),
                onGoBack = { finish() },
                weatherSummary = weatherSummary,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BriefingScreen(
    viewModel: BreifingViewModel,
    onGoBack: () -> Unit,
    weatherSummary: WeatherSummary?,
) {
    val context = LocalContext.current
    val gemmaState by viewModel.gemmaState.collectAsStateWithLifecycle()

    var currentResponse by remember { mutableStateOf("") }
    var currentIsResponding by remember { mutableStateOf(false) }

    when (val state = gemmaState) {
        is GemmaState.Ready -> {
            val localSessionManager = state.sessionManager
            currentResponse = localSessionManager.response.collectAsState().value
            currentIsResponding = localSessionManager.responding.collectAsState().value
        }

        else -> {
            currentResponse = ""
            currentIsResponding = false
        }
    }

    Scaffold(topBar = {
        TopAppBar(
            title = { Text("브리핑") },
            navigationIcon = {
                IconButton(onClick = onGoBack) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "뒤로 가기"
                    )
                }
            },
            actions = {
                IconButton(
                    onClick = {
                        ExternalAppUtils.shareText(
                            context,
                            currentResponse
                        )
                    },
                    enabled = currentResponse.isNotEmpty() && !currentIsResponding
                ) {
                    Icon(
                        imageVector = Icons.Filled.Share,
                        contentDescription = "공유"
                    )
                }
            }
        )
    }) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            when (val state = gemmaState) {
                is GemmaState.Idle -> {
                    Text("GemmaState Idle")
                }

                is GemmaState.Loading -> {
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .padding(8.dp)
                            .fillMaxSize()
                    ) {
                        CircularProgressIndicator()
                    }
                }

                is GemmaState.Error -> {
                    Text("오류 발생, Logcat 확인")
                }

                is GemmaState.Ready -> {
                    val scrollableState = rememberScrollState()

                    val prompt = PromptProvider.getFullPrompt(weatherSummary)
                    val localSessionManager = state.sessionManager
                    val response by localSessionManager.response.collectAsState()
                    val isResponding by localSessionManager.responding.collectAsState()

                    LaunchedEffect(prompt) {
                        if (!isResponding) {
                            Log.d("[Gemma Prompt]", prompt)
                            localSessionManager.sendQuery(prompt)
                        }
                    }

                    Column(
                        modifier = Modifier
                            .verticalScroll(scrollableState)
                            .fillMaxSize()
                            .padding(vertical = 16.dp)
                    ) {
                        if (response.isNotEmpty()) {
                            Text(response)
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = {
                                Log.d("[Gemma Prompt]", prompt)
                                localSessionManager.sendQuery(prompt)
                            },
                            enabled = !isResponding,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("브리핑 생성")
                        }
                    }
                }
            }
        }
    }
}