package com.pnu.myweather.feature.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.pnu.myweather.ui.theme.White

@Composable
fun Card(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = White),
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight(),
    ) {
        androidx.compose.foundation.layout.Box(modifier = Modifier.padding(16.dp)) {
            content()
        }
    }
}