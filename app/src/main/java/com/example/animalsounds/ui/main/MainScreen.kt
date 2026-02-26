package com.example.animalsounds.ui.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.animalsounds.ui.components.AnimalCard
import com.example.animalsounds.viewmodel.AnimalViewModel

/**
 * メイン画面。
 * 動物を 2 列グリッドで表示する。各カードをタップすると鳴き声＋アニメーションが発動する。
 */
@Composable
fun MainScreen(
    animalViewModel: AnimalViewModel = viewModel()
) {
    val playingAnimalId by animalViewModel.playingAnimalId.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFF8E1))
    ) {
        // ── ヘッダー ──────────────────────────────────────────────────────────
        // statusBarsPadding() でステータスバー分の余白を確保（edge-to-edge 対応）
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFFF8F00))
                .statusBarsPadding()
                .padding(vertical = 18.dp, horizontal = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "どうぶつさうんど 🎵",
                fontSize = 30.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White,
                textAlign = TextAlign.Center
            )
        }

        // ── 動物グリッド（幅 160dp を最小単位として列数を自動調整）
        // スマホ縦: 2列 / タブレット横: 3〜4列 と自動的に変化する
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 160.dp),
            contentPadding = PaddingValues(
                start = 16.dp,
                end = 16.dp,
                top = 16.dp,
                // navigationBarsPadding 相当をコンテンツ末尾に追加
                bottom = 24.dp
            ),
            modifier = Modifier
                .fillMaxSize()
                // ナビゲーションバー分の余白（ジェスチャーナビ対応）
                .windowInsetsPadding(WindowInsets.navigationBars),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(
                items = animalViewModel.animals,
                key = { animal -> animal.id }
            ) { animal ->
                AnimalCard(
                    animal = animal,
                    isPlaying = playingAnimalId == animal.id,
                    onTap = { animalViewModel.onAnimalTapped(animal) }
                )
            }
        }
    }
}
