package com.pnu.myweather.feature.setting.view

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.pnu.myweather.feature.component.Card
import com.pnu.myweather.feature.component.MyButton
import com.pnu.myweather.ui.theme.MyweatherTheme
import com.pnu.myweather.ui.theme.White

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
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("설정 지역", style = MaterialTheme.typography.titleMedium)
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Filled.Place,
                                    contentDescription = "위치 아이콘",
                                    modifier = Modifier.padding(end = 4.dp)
                                )
                                Text(
                                    text = "$sido $gu $dong",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("좌표(x/y)", style = MaterialTheme.typography.bodyMedium)
                            Text("$nx/$ny", fontSize = 16.sp, fontWeight = FontWeight.Medium)
                        }
                    }
                }

                Spacer(Modifier.height(32.dp))
            } else {
                Text("아직 지역 설정이 완료되지 않았습니다.", fontSize = 14.sp)
                Spacer(Modifier.height(32.dp))
            }

            MyButton(onClick = onEditClick) {
                Text("직접 설정", color = White, fontSize = 16.sp)
            }
        }
    }
}
