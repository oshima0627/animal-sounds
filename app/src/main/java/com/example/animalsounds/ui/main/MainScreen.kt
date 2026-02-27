package com.example.animalsounds.ui.main

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.animalsounds.data.model.ActiveAnimal
import com.example.animalsounds.data.model.AnimalPhase
import com.example.animalsounds.viewmodel.AnimalViewModel
import kotlinx.coroutines.launch
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin
import kotlin.random.Random

/**
 * ゲーム画面。
 * 3匹の動物が自由に動き回り、タップすると鳴き声を鳴らして逃げる。
 */
@Composable
fun MainScreen(
    animalViewModel: AnimalViewModel = viewModel()
) {
    val activeAnimals by animalViewModel.activeAnimals.collectAsState()

    LaunchedEffect(Unit) {
        animalViewModel.startGame()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFF8E1))
            .statusBarsPadding()
    ) {
        BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
            val screenWidthPx = constraints.maxWidth.toFloat()
            val screenHeightPx = constraints.maxHeight.toFloat()
            val isTablet = maxWidth >= 600.dp

            activeAnimals.forEach { activeAnimal ->
                key(activeAnimal.instanceId) {
                    MovingAnimalSprite(
                        activeAnimal = activeAnimal,
                        screenWidth = screenWidthPx,
                        screenHeight = screenHeightPx,
                        isTablet = isTablet,
                        onClick = { animalViewModel.onAnimalClicked(activeAnimal.instanceId) }
                    )
                }
            }
        }
    }
}

/**
 * 画面上を自由に動き回る動物スプライト。
 * - MOVING: ランダムに画面内を移動（壁で跳ね返る）
 * - SHAKING: 揺れアニメーション＋鳴き声テキスト表示
 * - ESCAPING: ランダム方向に飛び出して消える
 */
@Composable
fun MovingAnimalSprite(
    activeAnimal: ActiveAnimal,
    screenWidth: Float,
    screenHeight: Float,
    isTablet: Boolean = false,
    onClick: () -> Unit
) {
    val density = LocalDensity.current
    val animalSizeDp = if (isTablet) (110 * 4).dp else 110.dp
    val animalSizePx = with(density) { animalSizeDp.toPx() }

    // 初期位置
    var posX by remember {
        mutableStateOf(activeAnimal.initialX * (screenWidth - animalSizePx))
    }
    var posY by remember {
        mutableStateOf(activeAnimal.initialY * (screenHeight - animalSizePx))
    }

    // ランダム速度（方向もランダム）タブレットは半分のスピード
    val speedMin = if (isTablet) 1.5f else 3f
    val speedMax = if (isTablet) 3f else 6f
    var velX by remember {
        mutableStateOf(
            if (Random.nextBoolean()) Random.nextFloat() * (speedMax - speedMin) + speedMin
            else -(Random.nextFloat() * (speedMax - speedMin) + speedMin)
        )
    }
    var velY by remember {
        mutableStateOf(
            if (Random.nextBoolean()) Random.nextFloat() * (speedMax - speedMin) + speedMin
            else -(Random.nextFloat() * (speedMax - speedMin) + speedMin)
        )
    }

    // 逃げアニメーション用（Animatable）
    val escapeAnimX = remember { Animatable(posX) }
    val escapeAnimY = remember { Animatable(posY) }

    // フェーズに応じた動き
    LaunchedEffect(activeAnimal.instanceId, activeAnimal.phase) {
        when (activeAnimal.phase) {
            AnimalPhase.MOVING -> {
                // フレームごとに位置を更新（壁で跳ね返る）
                while (true) {
                    withFrameMillis {
                        var nx = posX + velX
                        var ny = posY + velY
                        val maxX = screenWidth - animalSizePx
                        val maxY = screenHeight - animalSizePx

                        if (nx < 0f || nx > maxX) {
                            velX = -velX
                            nx = nx.coerceIn(0f, maxX)
                        }
                        if (ny < 0f || ny > maxY) {
                            velY = -velY
                            ny = ny.coerceIn(0f, maxY)
                        }
                        posX = nx
                        posY = ny
                    }
                }
            }

            AnimalPhase.SHAKING -> {
                // 揺れ中は移動しない（位置固定）
            }

            AnimalPhase.ESCAPING -> {
                // 現在位置からランダム方向に飛び出す
                val angle = activeAnimal.escapeAngle
                val distance = maxOf(screenWidth, screenHeight) * 2f
                escapeAnimX.snapTo(posX)
                escapeAnimY.snapTo(posY)
                launch {
                    escapeAnimX.animateTo(
                        targetValue = posX + cos(angle) * distance,
                        animationSpec = tween(700, easing = FastOutLinearInEasing)
                    )
                }
                escapeAnimY.animateTo(
                    targetValue = posY + sin(angle) * distance,
                    animationSpec = tween(700, easing = FastOutLinearInEasing)
                )
            }
        }
    }

    // 最終的な表示位置
    val displayX = if (activeAnimal.phase == AnimalPhase.ESCAPING) escapeAnimX.value else posX
    val displayY = if (activeAnimal.phase == AnimalPhase.ESCAPING) escapeAnimY.value else posY

    // 揺れアニメーション（SHAKING時）
    val infiniteTransition = rememberInfiniteTransition(label = "shake_${activeAnimal.instanceId}")
    val shakeRotation by infiniteTransition.animateFloat(
        initialValue = -18f,
        targetValue = 18f,
        animationSpec = infiniteRepeatable(
            animation = tween(80, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "rotation_${activeAnimal.instanceId}"
    )
    val rotation = if (activeAnimal.phase == AnimalPhase.SHAKING) shakeRotation else 0f

    val context = LocalContext.current
    val imageResId = remember(activeAnimal.animal.imageResName) {
        context.resources.getIdentifier(
            activeAnimal.animal.imageResName, "drawable", context.packageName
        )
    }

    Box(
        modifier = Modifier
            .offset { IntOffset(displayX.roundToInt(), displayY.roundToInt()) }
            .size(animalSizeDp)
            .rotate(rotation)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            )
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            // 動物画像（またはフォールバックとして絵文字）
            if (imageResId != 0) {
                Image(
                    painter = painterResource(id = imageResId),
                    contentDescription = activeAnimal.animal.nameJp,
                    modifier = Modifier.size(if (isTablet) (76 * 4).dp else 76.dp)
                )
            } else {
                Text(
                    text = activeAnimal.animal.emoji,
                    fontSize = if (isTablet) (64 * 4).sp else 64.sp,
                    textAlign = TextAlign.Center
                )
            }

            // 動物名
            Text(
                text = activeAnimal.animal.nameJp,
                fontSize = if (isTablet) (14 * 4).sp else 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF3E2723),
                textAlign = TextAlign.Center
            )

            // 鳴き声テキスト（SHAKING時のみ表示）
            if (activeAnimal.phase == AnimalPhase.SHAKING) {
                Text(
                    text = activeAnimal.animal.soundText,
                    fontSize = if (isTablet) (13 * 4).sp else 13.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFFE65100),
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .background(
                            Color(0xFFFFF9C4),
                            RoundedCornerShape(8.dp)
                        )
                        .padding(horizontal = 4.dp, vertical = 2.dp)
                )
            }
        }
    }
}

/**
 * フレームごとに処理を実行するユーティリティ。
 * Compose の withFrameMillis をラップして使いやすくする。
 */
private suspend fun withFrameMillis(block: () -> Unit) {
    androidx.compose.runtime.withFrameMillis { block() }
}
