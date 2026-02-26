package com.example.animalsounds.data.model

/**
 * 動物データモデル
 *
 * @param id          一意のID（英語）
 * @param nameJp      ひらがな名前（表示用）
 * @param soundText   鳴き声テキスト（カタカナ）
 * @param emoji       絵文字（画像が無い場合のフォールバック・プレビュー用）
 * @param backgroundColor カードの背景色 (0xAARRGGBB)
 * @param soundResName  res/raw/ 以下の音声ファイル名（拡張子なし）
 */
data class Animal(
    val id: String,
    val nameJp: String,
    val soundText: String,
    val emoji: String,
    val backgroundColor: Long,
    val soundResName: String
)
