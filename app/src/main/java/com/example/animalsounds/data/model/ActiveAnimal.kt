package com.example.animalsounds.data.model

enum class AnimalPhase {
    MOVING,   // ランダムに動き回る
    SHAKING,  // クリックされて揺れている
    ESCAPING  // 逃げながら消えていく
}

data class ActiveAnimal(
    val instanceId: String,
    val animal: Animal,
    val phase: AnimalPhase = AnimalPhase.MOVING,
    val initialX: Float = 0.3f,   // 初期X位置（0..1の画面幅比）
    val initialY: Float = 0.3f,   // 初期Y位置（0..1の画面高さ比）
    val escapeAngle: Float = 0f   // 逃げる方向（ラジアン）
)
