# 音声ファイルの配置フォルダ

このフォルダに動物の鳴き声 MP3 ファイルを配置してください。

## 必要なファイル一覧

### 家畜・ペット

| ファイル名           | 動物     | 鳴き声テキスト  |
|---------------------|---------|--------------|
| sound_dog.mp3       | いぬ    | ワンワン！     |
| sound_cat.mp3       | ねこ    | ニャーニャー！  |
| sound_horse.mp3     | うま    | ヒヒーン！     |
| sound_cow.mp3       | うし    | モーモー！     |
| sound_chicken.mp3   | にわとり | コケコッコ！   |
| sound_chick.mp3     | ひよこ  | ピヨピヨ！     |
| sound_sheep.mp3     | ひつじ  | メェーメェー！  |
| sound_goat.mp3      | やぎ    | メーメー！     |

### 野生動物

| ファイル名            | 動物     | 鳴き声テキスト   |
|----------------------|---------|---------------|
| sound_lion.mp3       | らいおん | ガオー！       |
| sound_elephant.mp3   | ぞう    | パオーン！     |
| sound_wolf.mp3       | おおかみ | アオーン！    |
| sound_boar.mp3       | いのしし | ブヒブヒ！    |
| sound_crow.mp3       | からす  | カーカー！     |
| sound_sparrow.mp3    | すずめ  | チュンチュン！  |

## ファイル要件

- 形式: MP3（推奨）または OGG Vorbis
- 長さ: 0.5〜3 秒程度
- サンプリングレート: 44100 Hz 推奨
- 命名規則: 小文字・アンダースコア区切り（例: sound_dog.mp3）

## アクセス方法（Kotlin コード内）

```kotlin
// AnimalRepository.kt 内の soundResName が "sound_dog" の場合
val resId = context.resources.getIdentifier("sound_dog", "raw", context.packageName)
soundPool.load(context, resId, 1)
```

## 著作権注意

ロイヤリティフリーの音源を使用してください。
- [freesound.org](https://freesound.org)
- [soundsnap.com](https://soundsnap.com)
- 自作録音 など
