package com.example.animalsounds.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.animalsounds.data.model.Animal

/**
 * 動物 1 匹分のカード。
 *
 * タップすると [onTap] が呼ばれ、[isPlaying] が true の間
 * バウンスアニメーションとパーティクルエフェクトが表示される。
 *
 * 2 歳児でも押しやすいように最低でも 48dp×48dp を確保し、
 * カード全面をタップ領域にしている。
 */
@Composable
fun AnimalCard(
    animal: Animal,
    isPlaying: Boolean,
    onTap: () -> Unit
) {
    // バウンス（拡大）アニメーション
    val scale by animateFloatAsState(
        targetValue = if (isPlaying) 1.12f else 1.0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMediumLow
        ),
        label = "cardScale"
    )

    val cardShape = RoundedCornerShape(24.dp)

    // TalkBack 用の読み上げテキスト:
    //   待機中 → 「いぬ。タップして鳴き声を聞く」
    //   再生中 → 「いぬ。ワンワン！」
    val a11yDescription = if (isPlaying) {
        "${animal.nameJp}。${animal.soundText}"
    } else {
        "${animal.nameJp}。タップして鳴き声を聞く"
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            // 縦横比を少し縦長に（カード感を出す）
            .aspectRatio(0.82f)
            .scale(scale)
            .shadow(
                elevation = if (isPlaying) 12.dp else 4.dp,
                shape = cardShape,
                ambientColor = Color(animal.backgroundColor),
                spotColor = Color(animal.backgroundColor)
            )
            .clip(cardShape)
            .background(Color(animal.backgroundColor))
            // アクセシビリティ: TalkBack 向け読み上げテキストとロール
            .semantics {
                contentDescription = a11yDescription
                role = Role.Button
            }
            // カード全体がタップ領域（ripple なし：幼児向け）
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onTap
            )
    ) {
        // ── カードのメインコンテンツ ─────────────────────────────────────
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // 動物絵文字（メインビジュアル）
            Text(
                text = animal.emoji,
                fontSize = 76.sp,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            // 動物名（ひらがな・大きめ）
            Text(
                text = animal.nameJp,
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF3E2723),
                textAlign = TextAlign.Center
            )

            // 鳴き声テキスト（再生中のみ・バウンスで出現）
            AnimatedVisibility(
                visible = isPlaying,
                enter = scaleIn(
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessMedium
                    )
                ) + fadeIn(),
                exit = fadeOut()
            ) {
                Text(
                    text = animal.soundText,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFF5D4037),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 6.dp)
                )
            }
        }

        // ── パーティクルエフェクト（カードの上にオーバーレイ） ──────────
        if (isPlaying) {
            ParticleEffect(
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}
