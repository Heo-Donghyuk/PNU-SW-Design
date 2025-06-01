package com.pnu.myweather.feature.main.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pnu.myweather.core.util.ExternalAppUtils.openBrowser
import com.pnu.myweather.core.util.getWeatherIconRes
import com.pnu.myweather.core.weather.WeatherSummary

@Composable
fun WeatherCard(
    weatherSummary: WeatherSummary?,
    location: String,
    fineDust: String,
    ultraFineDust: String,
    weatherURL: String,
) {
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            // 좌측 정보
            Column(
                modifier = Modifier
                    .weight(1f)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Filled.Place,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(location, fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    weatherSummary?.temperature ?: "--",
                    fontWeight = FontWeight.Bold,
                    fontSize = 64.sp
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    weatherSummary?.skyState ?: "--",
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    "${weatherSummary?.maxTemp ?: "--"} / ${weatherSummary?.minTemp ?: "--"}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

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

                Text("미세먼지 $fineDust", fontSize = 16.sp)
                Text("초미세먼지 $ultraFineDust", fontSize = 16.sp)
            }

            // 우측 아이콘
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

        // 하단 "상세보기"
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "상세보기",
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .clickable {
                    openBrowser(
                        context = context,
                        weatherURL
                    )
                },
            color = Color.Gray,
            fontSize = 14.sp,
            textAlign = TextAlign.Center
        )
    }
}