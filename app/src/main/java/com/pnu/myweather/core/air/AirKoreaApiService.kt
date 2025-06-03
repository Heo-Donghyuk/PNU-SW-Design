package com.pnu.myweather.core.air

import retrofit2.http.GET
import retrofit2.http.Query

interface AirKoreaApiService {
    @GET("getMsrstnAcctoRltmMesureDnsty")
    suspend fun getAirQuality(
        @Query("stationName") stationName: String,
        @Query("dataTerm") dataTerm: String = "DAILY",
        @Query("pageNo") pageNo: Int = 1,
        @Query("numOfRows") numOfRows: Int = 1,
        @Query("returnType") returnType: String = "json",
        @Query("serviceKey") serviceKey: String,
        @Query("ver") ver: String = "1.3"
    ): AirKoreaResponse
}