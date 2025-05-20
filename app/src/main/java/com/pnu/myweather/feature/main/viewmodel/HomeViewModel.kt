package com.pnu.myweather.feature.main.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class HomeViewModel : ViewModel() {
    private val _naverWeatherUrl =
        MutableStateFlow("https://m.search.naver.com/search.naver?query=부산광역시 금정구 날씨")
    val naverWeatherUrl: StateFlow<String> = _naverWeatherUrl

    fun updateMessage(newUrl: String) {
        _naverWeatherUrl.value = newUrl
    }
}