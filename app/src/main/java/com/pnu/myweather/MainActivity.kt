package com.pnu.myweather

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import com.pnu.myweather.feature.main.view.HomeScreenActivity

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val intent = Intent(this, HomeScreenActivity::class.java)
        startActivity(intent)
        finish()
    }
}