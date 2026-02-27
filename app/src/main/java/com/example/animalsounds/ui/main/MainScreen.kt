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
import androidx.compose.runtime.mutableStateMapOf
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
import androidx.compose.ui.unit.Dp
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
import kotlin.math.sqrt
import kotlin.random.Random

/**
 * 各動物の位置・速度を保持する。
 * posX/posY は Compose ステートなので変化時に再コンポーズされる。
 */
private class AnimalPositionState(
    initPosX: Float,
    initPosY: Float,
    initVelX: Float,
    initVelY: Float,
) {
    var posX by mutableStateOf(initPosX)
    var posY by mutableStateOf(initPosY)
    var velX = initVelX
    var velY = initVelY
}

/**
 * ゲーム画面。
 * 3匹の動物が自由に動き回り、タップすると鳴き声を鳴らして逃げる。
 * 動物同士が重ならないよう衝突検出・反発を行う。
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
            val density = LocalDensity.current
            val animalSizeDp = if (isTablet) (110 * 4).dp else 110.dp
            val animalSizePx = with(density) { animalSizeDp.toPx() }
            val speedMin = if (isTablet) 1.5f else 3f
            val speedMax = if (isTablet) 3f else 6f

            // 全動物の位置・速度を一元管理（衝突検出のために共有）
            val positionStates = remember { mutableStateMapOf<String, AnimalPositionState>() }

            // 動物リストが変わったとき：新しい動物の状態を追加、消えた動物の状態を削除
            LaunchedEffect(activeAnimals) {
                val currentIds = activeAnimals.map { it.instanceId }.toSet()
                positionStates.keys.retainAll(currentIds)
                activeAnimals.forEach { animal ->
                    if (animal.instanceId !in positionStates) {
                        fun randVel() = if (Random.nextBoolean())
                            Random.nextFloat() * (speedMax - speedMin) + speedMin
                        else -(Random.nextFloat() * (speedMax - speedMin) + speedMin)

                        positionStates[animal.instanceId] = AnimalPositionState(
                            initPosX = animal.initialX * (screenWidthPx - animalSizePx),
                            initPosY = animal.initialY * (screenHeightPx - animalSizePx),
                            initVelX = randVel(),
                            initVelY = randVel()
                        )
                    }
                }
            }

            // MOVING 状態の動物IDリスト（フェーズが変わると再計算される）
            val movingIds = remember(activeAnimals) {
                activeAnimals.filter { it.phase == AnimalPhase.MOVING }.map { it.instanceId }
            }

            // 全 MOVING 動物を一つのループで更新（壁バウンド＋衝突検出）
            LaunchedEffect(movingIds) {
                while (true) {
                    withFrameMillis {
                        val states = movingIds.mapNotNull { id -> positionStates[id]?.let { id to it } }
                        val maxX = screenWidthPx - animalSizePx
                        val maxY = screenHeightPx - animalSizePx

                        // 位置更新 + 壁バウンド
                        states.forEach { (_, s) ->
                            var nx = s.posX + s.velX
                            var ny = s.posY + s.velY
                            if (nx < 0f || nx > maxX) { s.velX = -s.velX; nx = nx.coerceIn(0f, maxX) }
                            if (ny < 0f || ny > maxY) { s.velY = -s.velY; ny = ny.coerceIn(0f, maxY) }
                            s.posX = nx
                            s.posY = ny
                        }

                        // 動物同士の衝突検出・弾性反発
                        for (i in states.indices) {
                            for (j in i + 1 until states.size) {
                                val s1 = states[i].second
                                val s2 = states[j].second
                                val cx1 = s1.posX + animalSizePx / 2f
                                val cy1 = s1.posY + animalSizePx / 2f
                                val cx2 = s2.posX + animalSizePx / 2f
                                val cy2 = s2.posY + animalSizePx / 2f
                                val dx = cx2 - cx1
                                val dy = cy2 - cy1
                                val distSq = dx * dx + dy * dy
                                if (distSq < animalSizePx * animalSizePx && distSq > 0f) {
                                    val dist = sqrt(distSq)
                                    val normX = dx / dist
                                    val normY = dy / dist
                                    val dvx = s1.velX - s2.velX
                                    val dvy = s1.velY - s2.velY
                                    val dot = dvx * normX + dvy * normY
                                    if (dot > 0f) { // 接近中のみ反発
                                        s1.velX -= dot * normX
                                        s1.velY -= dot * normY
                                        s2.velX += dot * normX
                                        s2.velY += dot * normY
                                    }
                                    // 重なり解消
                                    val overlap = (animalSizePx - dist) / 2f
                                    s1.posX -= normX * overlap
                                    s1.posY -= normY * overlap
                                    s2.posX += normX * overlap
                                    s2.posY += normY * overlap
                                }
                            }
                        }
                    }
                }
            }

            // 動物スプライトを描画
            activeAnimals.forEach { activeAnimal ->
                key(activeAnimal.instanceId) {
                    val posState = positionStates[activeAnimal.instanceId]
                    if (posState != null) {
                        MovingAnimalSprite(
                            activeAnimal = activeAnimal,
                            posX = posState.posX,
                            posY = posState.posY,
                            screenWidth = screenWidthPx,
                            screenHeight = screenHeightPx,
                            isTablet = isTablet,
                            animalSizeDp = animalSizeDp,
                            onClick = { animalViewModel.onAnimalClicked(activeAnimal.instanceId) }
                        )
                    }
                }
            }

            // タップした動物の名前・鳴き声を画面中央に表示（揺れなし）
            val shakingAnimal = activeAnimals.find { it.phase == AnimalPhase.SHAKING }
            if (shakingAnimal != null) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .background(Color(0xEEFFF9C4), RoundedCornerShape(24.dp))
                            .padding(horizontal = 40.dp, vertical = 24.dp)
                    ) {
                        Text(
                            text = shakingAnimal.animal.nameJp,
                            fontSize = if (isTablet) 72.sp else 36.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color(0xFF3E2723),
                            textAlign = TextAlign.Center
                        )
                        Text(
                            text = shakingAnimal.animal.soundText,
                            fontSize = if (isTablet) 64.sp else 32.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color(0xFFE65100),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}

/**
 * 動物スプライト。位置は親から受け取る。
 * - MOVING: 親のループで移動（ここでは描画のみ）
 * - SHAKING: 画像が揺れる（名前・鳴き声は画面中央に別表示）
 * - ESCAPING: ランダム方向に飛び出して消える
 */
@Composable
fun MovingAnimalSprite(
    activeAnimal: ActiveAnimal,
    posX: Float,
    posY: Float,
    screenWidth: Float,
    screenHeight: Float,
    isTablet: Boolean = false,
    animalSizeDp: Dp,
    onClick: () -> Unit
) {
    // 逃げアニメーション用
    val escapeAnimX = remember { Animatable(posX) }
    val escapeAnimY = remember { Animatable(posY) }

    LaunchedEffect(activeAnimal.instanceId, activeAnimal.phase) {
        when (activeAnimal.phase) {
            AnimalPhase.MOVING -> { /* 移動は親のループで処理 */ }
            AnimalPhase.SHAKING -> { /* 揺れ中は固定 */ }
            AnimalPhase.ESCAPING -> {
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
        contentAlignment = Alignment.Center,
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
    }
}

/**
 * フレームごとに処理を実行するユーティリティ。
 */
private suspend fun withFrameMillis(block: () -> Unit) {
    androidx.compose.runtime.withFrameMillis { block() }
}
