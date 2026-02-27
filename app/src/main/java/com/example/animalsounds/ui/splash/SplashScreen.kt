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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.animalsounds.R
import com.example.animalsounds.ui.theme.DarkOnBackground
import com.example.animalsounds.ui.theme.DarkSplashBackground
import com.example.animalsounds.ui.theme.OrangePrimary
import com.example.animalsounds.ui.theme.SplashBackground
import com.example.animalsounds.ui.theme.TextMedium

/**
 * タイトル画面。
 * ロゴとタイトルをアニメーションで表示し、大きな「スタート」ボタンを表示する。
 * ボタンを押すとゲーム画面に遷移する。
 */
@Composable
fun SplashScreen(onNavigateToMain: () -> Unit) {
    var isVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        isVisible = true
    }

    val isDark = isSystemInDarkTheme()
    val bgColor = if (isDark) DarkSplashBackground else SplashBackground
    val titleColor = if (isDark) DarkOnBackground else TextMedium

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
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.padding(32.dp)
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

                Spacer(modifier = Modifier.height(48.dp))

                // 大きなスタートボタン
                Button(
                    onClick = onNavigateToMain,
                    modifier = Modifier
                        .width(220.dp)
                        .height(80.dp),
                    shape = RoundedCornerShape(40.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = OrangePrimary,
                        contentColor = Color.White
                    )
                ) {
                    Text(
                        text = "🎮 スタート！",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
            }
        }
    }
}
