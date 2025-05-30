package com.pnu.myweather.feature.main.view

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp

@Composable
fun SecretAppBarTitle(title: String, onUnlock: () -> Unit) {
    var clickCount by remember { mutableStateOf(0) }

    Text(
        text = title,
        modifier = Modifier
            .padding(8.dp)
            .pointerInput(Unit) {
                detectTapGestures(onTap = {
                    clickCount++
                    if (clickCount >= 5) {
                        onUnlock()
                        clickCount = 0
                    }
                })
            }
    )
}