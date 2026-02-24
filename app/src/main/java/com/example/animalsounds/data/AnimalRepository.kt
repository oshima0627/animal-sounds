package com.example.animalsounds.data

import com.example.animalsounds.data.model.Animal

/**
 * 動物データのリポジトリ
 * 身近な動物（犬・猫・うさぎ）と動物園の動物（ライオン・ゾウ・キリン）を提供する。
 */
object AnimalRepository {

    val animals: List<Animal> = listOf(
        // ── 身近な動物 ─────────────────────────────────────
        Animal(
            id = "dog",
            nameJp = "いぬ",
            soundText = "ワンワン！",
            emoji = "🐕",
            backgroundColor = 0xFFFFD7A8,
            soundResName = "sound_dog"
        ),
        Animal(
            id = "cat",
            nameJp = "ねこ",
            soundText = "ニャーニャー！",
            emoji = "🐈",
            backgroundColor = 0xFFFFB3C6,
            soundResName = "sound_cat"
        ),
        Animal(
            id = "rabbit",
            nameJp = "うさぎ",
            soundText = "ぴょんぴょん！",
            emoji = "🐇",
            backgroundColor = 0xFFD7F0FF,
            soundResName = "sound_rabbit"
        ),
        // ── 動物園の動物 ────────────────────────────────────
        Animal(
            id = "lion",
            nameJp = "らいおん",
            soundText = "ガオー！",
            emoji = "🦁",
            backgroundColor = 0xFFFFF0A8,
            soundResName = "sound_lion"
        ),
        Animal(
            id = "elephant",
            nameJp = "ぞう",
            soundText = "パオーン！",
            emoji = "🐘",
            backgroundColor = 0xFFD8D0F0,
            soundResName = "sound_elephant"
        ),
        Animal(
            id = "giraffe",
            nameJp = "きりん",
            soundText = "もぐもぐ～",
            emoji = "🦒",
            backgroundColor = 0xFFF0FFD0,
            soundResName = "sound_giraffe"
        )
    )
}
