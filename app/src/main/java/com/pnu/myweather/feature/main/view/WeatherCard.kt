package com.pnu.myweather.feature.main.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.outlined.Umbrella
import androidx.compose.material.icons.outlined.WaterDrop
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pnu.myweather.core.util.getWeatherIconRes
import com.pnu.myweather.core.weather.WeatherSummary

@Composable
fun WeatherCard(
    weatherSummary: WeatherSummary?,
    location: String,
    fineDust: String,
    ultraFineDust: String,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        // 왼쪽 정보 카드
        Column(
            modifier = Modifier
                .weight(1f)
        ) {
            // 지역명(지역명)
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Filled.Place, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text(location, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(8.dp))

            // 현재 기온
            Text(
                weatherSummary?.temperature ?: "--",
                fontWeight = FontWeight.Bold,
                fontSize = 64.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            // 하늘 상태
            Text(
                weatherSummary?.skyState ?: "--",
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp
            )
            Spacer(modifier = Modifier.height(10.dp))

            // 최고/최저 기온
            Text(
                "${weatherSummary?.maxTemp ?: "--"} / ${weatherSummary?.minTemp ?: "--"}",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            // 습도 및 강수
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Outlined.WaterDrop,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
                Text(weatherSummary?.humidity ?: "--")
                Spacer(modifier = Modifier.width(12.dp))
                Icon(
                    Icons.Outlined.Umbrella,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
                Text(weatherSummary?.precipitation ?: "--")
            }

            Spacer(modifier = Modifier.height(8.dp))

            // 미세먼지 정보
            Text("미세먼지 $fineDust")
            Text("초미세먼지 $ultraFineDust")
        }

        // 오른쪽 날씨 아이콘
        Box(
            modifier = Modifier
                .size(120.dp)
                .align(Alignment.CenterVertically),
            contentAlignment = Alignment.Center
        ) {
            val iconRes = getWeatherIconRes(weatherSummary?.skyState)
            Image(
                painter = painterResource(id = iconRes),
                contentDescription = "날씨 아이콘",
                modifier = Modifier.fillMaxSize(0.9f)
            )
        }
    }
}