package com.pnu.myweather.feature.setting.view

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SettingScreen(onGoBack: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("⚙️ Setting Screen")
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = onGoBack) {
            Text("뒤로 가기")
        }
    }
}