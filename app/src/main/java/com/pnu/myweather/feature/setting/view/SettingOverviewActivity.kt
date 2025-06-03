package com.pnu.myweather.feature.setting.view

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

class SettingOverviewActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SettingOverviewScreen(
                onEditClick = {
                    val intent = Intent(this, SettingScreenActivity::class.java)
                    startActivity(intent)
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingOverviewScreen(onEditClick: () -> Unit) {
    val context = LocalContext.current
    val sido = LocationPreference.getSido(context)
    val gu = LocationPreference.getGu(context)
    val dong = LocationPreference.getDong(context)
    val nx = LocationPreference.getNx(context)
    val ny = LocationPreference.getNy(context)

    val isConfigured = sido.isNotBlank() && gu.isNotBlank() && dong.isNotBlank()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("설정") })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (isConfigured) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("설정 지역", style = MaterialTheme.typography.titleMedium)
                        Spacer(Modifier.height(8.dp))
                        Text(text = "$sido $gu $dong")
                        Spacer(Modifier.height(4.dp))
                        Text(text = "좌표(x/y): $nx / $ny")
                    }
                }

                Spacer(Modifier.height(32.dp))
            } else {
                Text("아직 지역 설정이 완료되지 않았습니다.")
                Spacer(Modifier.height(32.dp))
            }

            Button(onClick = onEditClick) {
                Text("직접 설정")
            }
        }
    }
}
