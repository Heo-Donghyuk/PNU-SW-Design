package com.pnu.myweather.feature.main.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.pnu.myweather.R

@Composable
fun SecretAppBarTitle(title: String, onUnlock: () -> Unit) {
    var clickCount by remember { mutableStateOf(0) }

    Row(
        verticalAlignment = Alignment.CenterVertically,
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
    ) {
        Image(
            painter = painterResource(id = R.drawable.snow),
            contentDescription = "날씨 아이콘",
            modifier = Modifier
                .size(26.dp)
                .padding(end = 4.dp)
        )
        Text(
            text = title,
            fontWeight = FontWeight.SemiBold,
            fontFamily = FontFamily.SansSerif,
        )
    }
}