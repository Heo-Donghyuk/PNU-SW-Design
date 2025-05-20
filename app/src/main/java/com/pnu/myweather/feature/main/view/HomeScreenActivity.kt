package com.pnu.myweather.feature.main.view

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.pnu.myweather.feature.briefing.view.BriefingScreenActivity
import com.pnu.myweather.feature.setting.view.SettingScreenActivity

class HomeScreenActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            HomeScreen(
                onGoToBriefing = {
                    startActivity(Intent(this, BriefingScreenActivity::class.java))
                },
                onGoToSetting = {
                    startActivity(Intent(this, SettingScreenActivity::class.java))
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onGoToBriefing: () -> Unit,
    onGoToSetting: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("MyWeather") },
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
                .padding(16.dp)
        ) {
            Button(onClick = onGoToBriefing) {
                Text("브리핑")
            }
        }
    }
}