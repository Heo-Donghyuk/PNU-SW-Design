package com.pnu.myweather.feature.briefing.view

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Share
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.pnu.myweather.core.gemma.GemmaSessionManager
import com.pnu.myweather.core.gemma.GemmaState
import com.pnu.myweather.core.gemma.PromptProvider
import com.pnu.myweather.core.util.ExternalAppUtils
import com.pnu.myweather.core.weather.WeatherSummary
import com.pnu.myweather.feature.briefing.viewmodel.BreifingViewModel
import com.pnu.myweather.feature.component.Card
import com.pnu.myweather.feature.component.MyButton
import com.pnu.myweather.ui.theme.MyweatherTheme

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
            MyweatherTheme {
                BriefingScreen(
                    viewModel = viewModel(),
                    onGoBack = { finish() },
                    weatherSummary = weatherSummary,
                )
            }
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

    var prompt by remember { mutableStateOf("") }
    var localSessionManager by remember { mutableStateOf<GemmaSessionManager?>(null) }
    var isResponding by remember { mutableStateOf(false) }
    var response by remember { mutableStateOf("") }

    when (val stateVal = gemmaState) {
        is GemmaState.Ready -> {
            val session = stateVal.sessionManager
            localSessionManager = session
            prompt = PromptProvider.getFullPrompt(context, weatherSummary)
            val respondingState by session.responding.collectAsState()
            isResponding = respondingState
            val responseState by session.response.collectAsState()
            response = responseState

            LaunchedEffect(gemmaState) {
                if (!session.responding.value) {
                    session.sendQuery(prompt)
                }
            }
        }

        else -> {
            localSessionManager = null
            prompt = ""
            isResponding = false
            response = ""
        }
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
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
                                response
                            )
                        },
                        enabled = response.isNotEmpty() && !isResponding
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Share,
                            contentDescription = "공유"
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
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    when (gemmaState) {
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
                            Column {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Icon(
                                        Icons.Filled.AutoAwesome,
                                        contentDescription = null,
                                        modifier = Modifier
                                            .size(20.dp)
                                    )
                                    Text(
                                        "AI 브리핑",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 20.sp
                                    )
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                                if (response.isNotEmpty()) {
                                    Text(response)
                                }
                            }

                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            MyButton(
                onClick = {
                    localSessionManager.let { session ->
                        Log.d("[Gemma Prompt]", prompt)
                        session?.sendQuery(prompt)
                    }
                },
                enabled = (gemmaState is GemmaState.Ready) && !isResponding
            ) {
                Text("브리핑 생성")
            }
        }
    }
}
