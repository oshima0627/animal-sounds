package com.example.animalsounds.viewmodel

import android.app.Application
import android.content.Context
import android.media.AudioAttributes
import android.media.SoundPool
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.animalsounds.data.AnimalRepository
import com.example.animalsounds.data.model.Animal
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * 動物一覧画面の ViewModel。
 *
 * - 動物タップ時の鳴き声再生（SoundPool）を管理する
 * - アニメーション中の連打を防止する
 * - [playingAnimalId] で現在アニメーション中の動物IDを流す
 */
class AnimalViewModel(application: Application) : AndroidViewModel(application) {

    // ── SoundPool ─────────────────────────────────────────────────────────────
    private val soundPool: SoundPool
    private val soundMap = mutableMapOf<String, Int>()   // animalId -> soundPoolId
    private val loadedSounds = mutableSetOf<Int>()        // 読み込み完了した soundPoolId

    // ── State ─────────────────────────────────────────────────────────────────
    /** 現在再生・アニメーション中の動物ID。null = アイドル */
    private val _playingAnimalId = MutableStateFlow<String?>(null)
    val playingAnimalId: StateFlow<String?> = _playingAnimalId.asStateFlow()

    val animals: List<Animal> = AnimalRepository.animals

    // ── アニメーション表示時間（ミリ秒） ──────────────────────────────────────
    private val animationDurationMs = 1500L

    init {
        val audioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_MEDIA)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()

        soundPool = SoundPool.Builder()
            .setMaxStreams(1)
            .setAudioAttributes(audioAttributes)
            .build()

        soundPool.setOnLoadCompleteListener { _, sampleId, status ->
            if (status == 0) {
                loadedSounds.add(sampleId)
            }
        }

        loadSounds(application)
    }

    /** res/raw/ 以下の音声ファイルを SoundPool に読み込む */
    private fun loadSounds(context: Context) {
        animals.forEach { animal ->
            val resId = context.resources.getIdentifier(
                animal.soundResName, "raw", context.packageName
            )
            if (resId != 0) {
                val soundId = soundPool.load(context, resId, 1)
                soundMap[animal.id] = soundId
            }
        }
    }

    /**
     * 動物がタップされたときに呼ぶ。
     * 再生中は連打を無視する（debounce）。
     */
    fun onAnimalTapped(animal: Animal) {
        // 連打防止：前のアニメーションが終わるまで無視
        if (_playingAnimalId.value != null) return

        viewModelScope.launch {
            _playingAnimalId.value = animal.id

            // 鳴き声再生（音声ファイルが存在し、読み込み完了している場合のみ）
            soundMap[animal.id]?.let { soundId ->
                if (loadedSounds.contains(soundId)) {
                    soundPool.play(soundId, 1.0f, 1.0f, 1, 0, 1.0f)
                }
            }

            delay(animationDurationMs)
            _playingAnimalId.value = null
        }
    }

    override fun onCleared() {
        super.onCleared()
        soundPool.release()
    }
}
