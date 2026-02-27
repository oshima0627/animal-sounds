package com.example.animalsounds.data

import com.example.animalsounds.data.model.Animal

/**
 * 動物データのリポジトリ
 * 家畜・ペット（犬・猫・馬・牛・にわとり・ひよこ・ひつじ・やぎ）と
 * 野生動物（ライオン・ゾウ・オオカミ・イノシシ・カラス・スズメ）を提供する。
 */
object AnimalRepository {

    val animals: List<Animal> = listOf(
        // ── 家畜・ペット ────────────────────────────────────────
        Animal(
            id = "dog",
            nameJp = "いぬ",
            soundText = "ワンワン！",
            emoji = "🐕",
            backgroundColor = 0xFFFFD7A8,
            soundResName = "sound_dog",
            imageResName = "img_dog"
        ),
        Animal(
            id = "cat",
            nameJp = "ねこ",
            soundText = "ニャーニャー！",
            emoji = "🐈",
            backgroundColor = 0xFFFFB3C6,
            soundResName = "sound_cat",
            imageResName = "img_cat"
        ),
        Animal(
            id = "horse",
            nameJp = "うま",
            soundText = "ヒヒーン！",
            emoji = "🐴",
            backgroundColor = 0xFFC8A070,
            soundResName = "sound_horse",
            imageResName = "img_horse"
        ),
        Animal(
            id = "cow",
            nameJp = "うし",
            soundText = "モーモー！",
            emoji = "🐄",
            backgroundColor = 0xFFF0F0E8,
            soundResName = "sound_cow",
            imageResName = "img_cow"
        ),
        Animal(
            id = "chicken",
            nameJp = "にわとり",
            soundText = "コケコッコ！",
            emoji = "🐓",
            backgroundColor = 0xFFFFCC80,
            soundResName = "sound_chicken",
            imageResName = "img_chicken"
        ),
        Animal(
            id = "chick",
            nameJp = "ひよこ",
            soundText = "ピヨピヨ！",
            emoji = "🐥",
            backgroundColor = 0xFFFFE860,
            soundResName = "sound_chick",
            imageResName = "img_chick"
        ),
        Animal(
            id = "sheep",
            nameJp = "ひつじ",
            soundText = "メェーメェー！",
            emoji = "🐑",
            backgroundColor = 0xFFE0E8E0,
            soundResName = "sound_sheep",
            imageResName = "img_sheep"
        ),
        Animal(
            id = "goat",
            nameJp = "やぎ",
            soundText = "メーメー！",
            emoji = "🐐",
            backgroundColor = 0xFFDDD8B8,
            soundResName = "sound_goat",
            imageResName = "img_goat"
        ),
        // ── 野生動物 ──────────────────────────────────────────────
        Animal(
            id = "lion",
            nameJp = "らいおん",
            soundText = "ガオー！",
            emoji = "🦁",
            backgroundColor = 0xFFFFF0A8,
            soundResName = "sound_lion",
            imageResName = "img_lion"
        ),
        Animal(
            id = "elephant",
            nameJp = "ぞう",
            soundText = "パオーン！",
            emoji = "🐘",
            backgroundColor = 0xFFD8D0F0,
            soundResName = "sound_elephant",
            imageResName = "img_elephant"
        ),
        Animal(
            id = "wolf",
            nameJp = "おおかみ",
            soundText = "アオーン！",
            emoji = "🐺",
            backgroundColor = 0xFF9AABBC,
            soundResName = "sound_wolf",
            imageResName = "img_wolf"
        ),
        Animal(
            id = "boar",
            nameJp = "いのしし",
            soundText = "ブヒブヒ！",
            emoji = "🐗",
            backgroundColor = 0xFFD4A07A,
            soundResName = "sound_boar",
            imageResName = "img_boar"
        ),
        Animal(
            id = "crow",
            nameJp = "からす",
            soundText = "カーカー！",
            emoji = "🐦‍⬛",
            backgroundColor = 0xFF8898A8,
            soundResName = "sound_crow",
            imageResName = "img_crow"
        ),
        Animal(
            id = "sparrow",
            nameJp = "すずめ",
            soundText = "チュンチュン！",
            emoji = "🐦",
            backgroundColor = 0xFFD0B890,
            soundResName = "sound_sparrow",
            imageResName = "img_sparrow"
        )
    )
}
