package com.pnu.myweather.feature.setting.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.pnu.myweather.ui.theme.White
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

import com.pnu.myweather.ui.theme.MyweatherTheme

class SettingOverviewActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var refreshKey by remember { mutableStateOf(0) }
            val lifecycleOwner = LocalLifecycleOwner.current

            DisposableEffect(lifecycleOwner) {
                val observer = LifecycleEventObserver { _, event ->
                    if (event == Lifecycle.Event.ON_RESUME) {
                        refreshKey++
                    }
                }
                lifecycleOwner.lifecycle.addObserver(observer)
                onDispose {
                    lifecycleOwner.lifecycle.removeObserver(observer)
                }
            }
            MyweatherTheme {
                SettingOverviewScreen(
                    refreshKey = refreshKey,
                    onEditClick = {
                        startActivity(Intent(this, SettingScreenActivity::class.java))
                    },
                    onGoBack = { finish() }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingOverviewScreen(
    refreshKey: Int,
    onGoBack: () -> Unit,
    onEditClick: () -> Unit
) {
    val context = LocalContext.current

    // 지역 정보를 remember state로 저장
    var sido by remember { mutableStateOf("") }
    var gu by remember { mutableStateOf("") }
    var dong by remember { mutableStateOf("") }
    var nx by remember { mutableStateOf(-1) }
    var ny by remember { mutableStateOf(-1) }

    //refreshKey가 바뀔 때마다 최신 값 로드
    LaunchedEffect(refreshKey) {
        sido = LocationPreference.getSido(context)
        gu = LocationPreference.getGu(context)
        dong = LocationPreference.getDong(context)
        nx = LocationPreference.getNx(context)
        ny = LocationPreference.getNy(context)
    }


    val isConfigured = sido.isNotBlank() && gu.isNotBlank() && dong.isNotBlank()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("설정", fontSize = 20.sp, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onGoBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "뒤로가기")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (isConfigured) {
               // Card(
                 //   modifier = Modifier.fillMaxWidth(),
                   // elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                //) {
                    com.pnu.myweather.feature.component.Card {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("설정 지역", style = MaterialTheme.typography.titleMedium)
                            Spacer(Modifier.height(8.dp))
                            Text(text = "$sido $gu $dong", fontSize = 16.sp)
                            Spacer(Modifier.height(4.dp))
                            Text(text = "좌표(x/y): $nx / $ny", fontSize = 14.sp)
                        }
                    }
                //}

                Spacer(Modifier.height(32.dp))
            } else {
                Text("아직 지역 설정이 완료되지 않았습니다.", fontSize = 14.sp)
                Spacer(Modifier.height(32.dp))
            }

            com.pnu.myweather.feature.component.MyButton(onClick = onEditClick) {
                Text("직접 설정", color = White, fontSize = 16.sp)
            }
        }
    }
}
