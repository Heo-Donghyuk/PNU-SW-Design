package com.pnu.myweather.core.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.pnu.myweather.feature.main.view.MainScreen
import com.pnu.myweather.feature.briefing.view.BriefingScreen
import com.pnu.myweather.feature.setting.view.SettingScreen

@Composable
fun AppNavGraph(navController: NavHostController) {
    NavHost(navController, startDestination = "main") {
        composable("main") {
            MainScreen(
                onGoToBriefing = { navController.navigate("briefing") },
                onGoToSetting = { navController.navigate("setting") }
            )
        }
        composable("briefing") {
            BriefingScreen(
                onGoBack = { navController.popBackStack() }
            )
        }
        composable("setting") {
            SettingScreen(
                onGoBack = { navController.popBackStack() }
            )
        }
    }
}