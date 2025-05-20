package com.pnu.myweather.feature.main.view

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun MainScreen(
    onGoToBriefing: () -> Unit,
    onGoToSetting: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("🌟 Main Screen")
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = onGoToBriefing) {
            Text("브리핑 화면으로")
        }
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = onGoToSetting) {
            Text("설정 화면으로")
        }
    }
}