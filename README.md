# どうぶつさうんど 🐾

2歳児向け動物タッチアプリ for Android

動物をタップすると**鳴き声**・**バウンスアニメーション**・**星/ハートのエフェクト**が発動するシンプルなアプリです。

---

## スクリーンショット

| スプラッシュ画面 | メイン画面（動物グリッド） |
|:---:|:---:|
| アプリ起動時にロゴがバウンスして表示 | 2列グリッドで6匹の動物を表示 |

---

## 登場動物

| 動物 | ひらがな | 鳴き声 |
|:---:|:---:|:---:|
| 🐕 犬 | いぬ | ワンワン！ |
| 🐈 猫 | ねこ | ニャーニャー！ |
| 🐇 うさぎ | うさぎ | ぴょんぴょん！ |
| 🦁 ライオン | らいおん | ガオー！ |
| 🐘 ゾウ | ぞう | パオーン！ |
| 🦒 キリン | きりん | もぐもぐ～ |

---

## 技術スタック

| 項目 | 技術 |
|:---|:---|
| 言語 | Kotlin 1.9.24 |
| UI | Jetpack Compose (BOM 2024.06.00) |
| アニメーション | Compose Animation API (spring / infinite) |
| 音声再生 | SoundPool (MediaPlayer API) |
| アーキテクチャ | MVVM (AndroidViewModel + StateFlow) |
| ナビゲーション | Navigation Compose 2.7.7 |
| ビルドツール | Gradle 8.4 (Kotlin DSL) |
| 最小SDK | API 26 (Android 8.0) |

---

## ディレクトリ構成

```
app/src/main/
├── java/com/example/animalsounds/
│   ├── MainActivity.kt               # エントリポイント
│   ├── data/
│   │   ├── model/Animal.kt           # 動物データモデル
│   │   └── AnimalRepository.kt      # 動物リスト（6匹）
│   ├── ui/
│   │   ├── navigation/AppNavigation.kt # NavHost 定義
│   │   ├── splash/SplashScreen.kt    # スプラッシュ画面
│   │   ├── main/MainScreen.kt        # メイン画面（グリッド）
│   │   ├── components/
│   │   │   ├── AnimalCard.kt         # 動物カード + アニメーション
│   │   │   └── ParticleEffect.kt    # 星/ハートのパーティクル
│   │   └── theme/
│   │       ├── Color.kt             # カラー定義
│   │       ├── Theme.kt             # Material3 テーマ
│   │       └── Type.kt              # タイポグラフィ
│   └── viewmodel/
│       └── AnimalViewModel.kt       # SoundPool管理・連打防止
└── res/
    ├── raw/                         # 🔊 音声ファイル（要追加）
    ├── drawable/                    # アダプティブアイコン素材
    ├── mipmap-anydpi-v26/           # アプリアイコン (API 26+)
    ├── values/
    │   ├── strings.xml
    │   ├── colors.xml
    │   └── themes.xml
    └── xml/
        ├── backup_rules.xml
        └── data_extraction_rules.xml
```

---

## セットアップ手順

### 前提条件

- Android Studio Hedgehog (2023.1.1) 以降
- JDK 17 以上
- Android SDK（API 26〜34）

### 1. リポジトリのクローン

```bash
git clone https://github.com/oshima0627/animal-sounds.git
cd animal-sounds
```

### 2. Gradle Wrapper の生成（初回のみ）

`gradle-wrapper.jar` はバイナリのためリポジトリに含まれていません。
以下いずれかの方法で生成してください。

**方法 A: Android Studio で開く（推奨）**
Android Studio で `animal-sounds/` フォルダを開くと自動でセットアップされます。

**方法 B: コマンドラインで生成**
```bash
# Gradle がインストール済みの場合
gradle wrapper --gradle-version 8.4
```

### 3. `local.properties` の作成

```properties
# Android SDK のパスを設定（各自の環境に合わせて変更）
sdk.dir=/Users/yourname/Library/Android/sdk    # macOS
# sdk.dir=/home/yourname/Android/Sdk           # Linux
# sdk.dir=C\:\\Users\\yourname\\AppData\\Local\\Android\\Sdk  # Windows
```

### 4. ビルドと実行

```bash
./gradlew assembleDebug
```

または Android Studio の `Run` ボタンを押す。

---

## 🔊 音声ファイルの追加方法

音声ファイルがなくてもアプリは起動します（鳴き声なし）。
鳴き声を追加するには、以下のファイルを `app/src/main/res/raw/` に配置してください。

| ファイル名 | 動物 |
|:---|:---|
| `sound_dog.mp3` | 犬（ワンワン） |
| `sound_cat.mp3` | 猫（ニャーニャー） |
| `sound_rabbit.mp3` | うさぎ |
| `sound_lion.mp3` | ライオン（ガオー） |
| `sound_elephant.mp3` | ゾウ（パオーン） |
| `sound_giraffe.mp3` | キリン |

**無料音源サイト:**
- [freesound.org](https://freesound.org) — CC0 / CC BY ライセンスの効果音多数
- [効果音ラボ](https://soundeffect-lab.info/) — 日本語サイト、商用利用可

> 音声を追加する場合は、ライセンスを確認しREADMEに出典を記載してください。

---

## 機能詳細

### タップ時の演出
1. **鳴き声再生** — `SoundPool` で低遅延再生
2. **バウンスアニメーション** — `spring(DampingRatioMediumBouncy)` でカード拡大
3. **鳴き声テキスト表示** — カード上にカタカナでポップアップ
4. **パーティクルエフェクト** — ⭐❤️✨🌟💫🎉が8粒、カードから飛び出してフェードアウト

### 連打防止
`AnimalViewModel.onAnimalTapped()` 内で `playingAnimalId != null` の間は新規タップを無視（1.5秒間）。

### オフライン動作
通信を一切使用しません。インターネット接続不要です。

---

## ライセンス

このプロジェクトのコードは [Apache License 2.0](LICENSE) の下で公開されています。

### 素材について
- 動物イラスト: 絵文字（Unicode / OS標準）を使用（ライセンス不要）
- 音声ファイル: 各自でご用意ください（`res/raw/` に配置）

---

## 今後の改善案

- [ ] 動物イラストを手書き風の画像に差し替え
- [ ] 動物ごとに異なるアニメーション（ライオンは咆哮、うさぎはジャンプ等）
- [ ] タブレット向け 3列グリッド対応
- [ ] 動物の数を増やす（ペンギン、パンダ等）
- [ ] 鳴き声の種類を複数登録してランダム再生
- [ ] 子供の操作ログ（親向け）
