package com.pnu.myweather.feature.setting.view

import android.content.Context
import android.content.SharedPreferences

object LocationPreference {

    private const val PREF_NAME = "location_pref"
    private const val KEY_SIDO = "sido"
    private const val KEY_GU = "gu"
    private const val KEY_DONG = "dong"
    private const val KEY_NX = "nx"
    private const val KEY_NY = "ny"
    private const val KEY_STATION = "station"

    private fun prefs(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    // 저장
    fun save(context: Context, sido: String, gu: String, dong: String, nx: Int, ny: Int,station: String) {
        prefs(context).edit().apply {
            putString(KEY_SIDO, sido)
            putString(KEY_GU, gu)
            putString(KEY_DONG, dong)
            putInt(KEY_NX, nx)
            putInt(KEY_NY, ny)
            putString(KEY_STATION, station)
            apply()
        }
    }
    // 초기화
    fun clear(context: Context) {
        prefs(context).edit().clear().apply()
    }



    // 불러오기
    fun getSido(context: Context): String = prefs(context).getString(KEY_SIDO, "") ?: ""
    fun getGu(context: Context): String = prefs(context).getString(KEY_GU, "") ?: ""
    fun getDong(context: Context): String = prefs(context).getString(KEY_DONG, "") ?: ""
    fun getNx(context: Context): Int = prefs(context).getInt(KEY_NX, -1)
    fun getNy(context: Context): Int = prefs(context).getInt(KEY_NY, -1)
    fun getStation(context: Context): String = prefs(context).getString(KEY_STATION, "") ?: ""

    fun getLocationString(context: Context): String {
        return "${getSido(context)} ${getGu(context)} ${getDong(context)}"
    }
}
