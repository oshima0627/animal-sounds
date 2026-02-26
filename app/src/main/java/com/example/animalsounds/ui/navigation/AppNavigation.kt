package com.example.animalsounds.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.animalsounds.ui.main.MainScreen
import com.example.animalsounds.ui.splash.SplashScreen

private const val ROUTE_SPLASH = "splash"
private const val ROUTE_MAIN = "main"

/**
 * アプリ全体のナビゲーション定義。
 *
 * スプラッシュ画面 → メイン画面（動物グリッド）の 2 画面構成。
 */
@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = ROUTE_SPLASH
    ) {
        composable(ROUTE_SPLASH) {
            SplashScreen(
                onNavigateToMain = {
                    navController.navigate(ROUTE_MAIN) {
                        // スプラッシュをバックスタックから除去（戻れないようにする）
                        popUpTo(ROUTE_SPLASH) { inclusive = true }
                    }
                }
            )
        }
        composable(ROUTE_MAIN) {
            MainScreen()
        }
    }
}
