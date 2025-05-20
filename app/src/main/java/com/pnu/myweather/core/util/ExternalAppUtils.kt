package com.pnu.myweather.core.util

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.core.net.toUri

object ExternalAppUtils {

    fun openBrowser(context: Context, url: String) {
        try {
            val browserIntent = Intent(Intent.ACTION_VIEW, url.toUri()).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) // 여기
            }
            context.startActivity(browserIntent)
        } catch (e: Exception) {
            Toast.makeText(context, "브라우저를 열 수 없습니다.", Toast.LENGTH_SHORT).show()
        }
    }

    fun shareText(context: Context, text: String) {
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, text)
        }
        context.startActivity(Intent.createChooser(shareIntent, "공유하기"))
    }
}