package com.pnu.myweather.core.air

import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object AirKoreaClient {
    private const val BASE_URL = "https://apis.data.go.kr/B552584/ArpltnInforInqireSvc/"

    val apiService: AirKoreaApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())  // ✅ Moshi로 변경
            .build()
            .create(AirKoreaApiService::class.java)
    }
}
