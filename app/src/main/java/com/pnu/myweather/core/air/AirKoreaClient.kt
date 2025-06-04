package com.pnu.myweather.core.air

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

object AirKoreaClient {
    private const val BASE_URL = "https://apis.data.go.kr/B552584/ArpltnInforInqireSvc/"

    private val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(logging)
        .connectTimeout(20, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient) // 👈 꼭 추가해야 함!
        .addConverterFactory(MoshiConverterFactory.create())
        .build()

    val apiService: AirKoreaApiService by lazy {
        retrofit.create(AirKoreaApiService::class.java)
    }
}