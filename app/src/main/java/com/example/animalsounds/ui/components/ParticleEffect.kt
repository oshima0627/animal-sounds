package com.example.animalsounds.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.sp
import kotlin.random.Random

/**
 * 星・ハートがカードからパラパラと飛び出してフェードアウトするエフェクト。
 *
 * [Modifier] で表示範囲を制御できる（通常は AnimalCard いっぱいに広げる）。
 */
@Composable
fun ParticleEffect(modifier: Modifier = Modifier) {
    val emojis = listOf("⭐", "❤️", "✨", "🌟", "💫", "🎉", "⭐", "❤️")

    // 各パーティクルの飛び先と遅延をランダムに決定（コンポーズ時に 1 回だけ）
    val particles = remember {
        (0 until 8).map { index ->
            ParticleData(
                emoji = emojis[index % emojis.size],
                // カード中央から外へ向かうランダムな方向ベクトル
                targetDx = Random.nextFloat() * 240f - 120f,
                targetDy = -(Random.nextFloat() * 180f + 60f),
                delayMs = index * 90
            )
        }
    }

    Box(modifier = modifier) {
        particles.forEach { particle ->
            ParticlePiece(particle = particle)
        }
    }
}

/** 1 粒のパーティクルを表すデータ */
private data class ParticleData(
    val emoji: String,
    val targetDx: Float,   // 目標 X 変位（dp 相当）
    val targetDy: Float,   // 目標 Y 変位（dp 相当、負 = 上方向）
    val delayMs: Int
)

@Composable
private fun ParticlePiece(particle: ParticleData) {
    val totalDurationMs = 950
    val infiniteTransition = rememberInfiniteTransition(label = "particle_${particle.emoji}")

    val progress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = totalDurationMs,
                delayMillis = particle.delayMs,
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Restart
        ),
        label = "particle_progress"
    )

    // 後半 30% でフェードアウト
    val alpha = when {
        progress < 0.70f -> 1f
        else -> (1f - progress) / 0.30f
    }

    Text(
        text = particle.emoji,
        fontSize = 22.sp,
        modifier = Modifier
            .fillMaxSize()
            // fillMaxSize で親 Box 全体を占有し、中央を基点に移動
            .wrapContentSize(unbounded = true)
            .graphicsLayer {
                translationX = particle.targetDx * progress
                translationY = particle.targetDy * progress
                this.alpha = alpha.coerceIn(0f, 1f)
            }
    )
}
