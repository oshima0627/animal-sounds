package com.example.animalsounds.ui.splash

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.animalsounds.R
import com.example.animalsounds.ui.theme.DarkOnBackground
import com.example.animalsounds.ui.theme.DarkSplashBackground
import com.example.animalsounds.ui.theme.SplashBackground
import com.example.animalsounds.ui.theme.TextLight
import com.example.animalsounds.ui.theme.TextMedium
import kotlinx.coroutines.delay

/** スプラッシュ表示時間（ミリ秒） */
private const val SPLASH_DURATION_MS = 2000L

/**
 * スプラッシュ画面。
 * アプリ起動時にロゴとタイトルをバウンスアニメーションで表示し、
 * [SPLASH_DURATION_MS] 後に [onNavigateToMain] を呼ぶ。
 *
 * ダークモード時は暖色系ダーク背景に切り替わる。
 */
@Composable
fun SplashScreen(onNavigateToMain: () -> Unit) {
    var isVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        isVisible = true
        delay(SPLASH_DURATION_MS)
        onNavigateToMain()
    }

    val isDark = isSystemInDarkTheme()
    val bgColor = if (isDark) DarkSplashBackground else SplashBackground
    val titleColor = if (isDark) DarkOnBackground else TextMedium
    val subtitleColor = if (isDark) DarkOnBackground.copy(alpha = 0.8f) else TextLight

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(bgColor),
        contentAlignment = Alignment.Center
    ) {
        AnimatedVisibility(
            visible = isVisible,
            enter = scaleIn(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                )
            ) + fadeIn()
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // アプリアイコン代わりの大きな絵文字
                Text(
                    text = stringResource(R.string.splash_emoji),
                    fontSize = 96.sp,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(24.dp))

                // アプリタイトル
                Text(
                    text = stringResource(R.string.splash_title),
                    fontSize = 44.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = titleColor,
                    textAlign = TextAlign.Center,
                    lineHeight = 56.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                // サブタイトル
                Text(
                    text = stringResource(R.string.splash_subtitle),
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Medium,
                    color = subtitleColor,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}
