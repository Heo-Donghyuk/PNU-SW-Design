package com.pnu.myweather.core.weather

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@SuppressLint("ParcelCreator")
@Parcelize
data class WeatherSummary(
    val temperature: String?,
    val skyState: String?,
    val minTemp: String?,
    val maxTemp: String?,
    val humidity: String?,
    val precipitation: String?
) : Parcelable