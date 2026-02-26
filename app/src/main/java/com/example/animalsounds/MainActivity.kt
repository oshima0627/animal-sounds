package com.example.animalsounds

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import com.example.animalsounds.ui.navigation.AppNavigation
import com.example.animalsounds.ui.theme.AnimalSoundsTheme

/**
 * アプリのエントリポイント。
 * Jetpack Compose でスプラッシュ → メイン画面を描画する。
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AnimalSoundsTheme {
                AppNavigation()
            }
        }
    }
}
