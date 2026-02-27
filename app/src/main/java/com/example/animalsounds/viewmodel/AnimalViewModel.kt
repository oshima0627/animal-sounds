package com.example.animalsounds.viewmodel

import android.app.Application
import android.content.Context
import android.media.AudioAttributes
import android.media.SoundPool
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.animalsounds.data.AnimalRepository
import com.example.animalsounds.data.model.ActiveAnimal
import com.example.animalsounds.data.model.AnimalPhase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.math.PI
import kotlin.random.Random

/**
 * ゲーム状態を管理する ViewModel。
 *
 * - ゲーム開始: 3匹の動物をランダムで選び、画面上をランダムに動かす
 * - タップ: 鳴き声再生 → 揺れアニメーション → 逃げて消える → 新しい動物が出現
 * - 常に3匹が画面上に表示される状態を維持する
 */
class AnimalViewModel(application: Application) : AndroidViewModel(application) {

    // ── SoundPool ─────────────────────────────────────────────────────────────
    private val soundPool: SoundPool
    private val soundMap = mutableMapOf<String, Int>()
    private val loadedSounds = mutableSetOf<Int>()

    // ── ゲーム状態 ─────────────────────────────────────────────────────────────
    private val _gameStarted = MutableStateFlow(false)
    val gameStarted: StateFlow<Boolean> = _gameStarted.asStateFlow()

    private val _activeAnimals = MutableStateFlow<List<ActiveAnimal>>(emptyList())
    val activeAnimals: StateFlow<List<ActiveAnimal>> = _activeAnimals.asStateFlow()

    init {
        val audioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_MEDIA)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()

        soundPool = SoundPool.Builder()
            .setMaxStreams(3)
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
        AnimalRepository.animals.forEach { animal ->
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
     * ゲームを開始する。
     * ランダムで選ばれた3匹の動物を画面上に配置する。
     */
    fun startGame() {
        _gameStarted.value = true
        _activeAnimals.value = createInitialAnimals()
    }

    private fun createInitialAnimals(): List<ActiveAnimal> {
        return AnimalRepository.animals.shuffled().take(3).mapIndexed { index, animal ->
            ActiveAnimal(
                instanceId = "${animal.id}_${System.currentTimeMillis()}_$index",
                animal = animal,
                phase = AnimalPhase.MOVING,
                initialX = Random.nextFloat() * 0.55f + 0.1f,
                initialY = Random.nextFloat() * 0.55f + 0.1f
            )
        }
    }

    /**
     * 動物がタップされたときに呼ぶ。
     * MOVING 状態の動物のみ反応する。
     * 鳴き声再生 → 揺れ → 逃げる → 新しい動物が出現
     */
    fun onAnimalClicked(instanceId: String) {
        val animal = _activeAnimals.value.find { it.instanceId == instanceId } ?: return
        if (animal.phase != AnimalPhase.MOVING) return

        // 鳴き声再生
        playSound(animal.animal.id)

        // SHAKING に変更
        updateAnimalPhase(instanceId, AnimalPhase.SHAKING)

        viewModelScope.launch {
            // 揺れている間（1.5秒）
            delay(1500L)

            // 逃げる方向をランダムに決定
            val escapeAngle = Random.nextFloat() * 2f * PI.toFloat()

            _activeAnimals.value = _activeAnimals.value.map {
                if (it.instanceId == instanceId) {
                    it.copy(phase = AnimalPhase.ESCAPING, escapeAngle = escapeAngle)
                } else it
            }

            // 逃げるアニメーション待ち（700ms）
            delay(700L)

            // 逃げた動物を除いた残り
            val remaining = _activeAnimals.value.filter { it.instanceId != instanceId }

            // 現在表示中の動物IDを除いて新しい動物を選ぶ
            val usedIds = remaining.map { it.animal.id }.toSet()
            val available = AnimalRepository.animals.filter { it.id !in usedIds }
            val newAnimal = (if (available.isNotEmpty()) available else AnimalRepository.animals).random()

            val newActive = ActiveAnimal(
                instanceId = "${newAnimal.id}_${System.currentTimeMillis()}",
                animal = newAnimal,
                phase = AnimalPhase.MOVING,
                initialX = Random.nextFloat() * 0.55f + 0.1f,
                initialY = Random.nextFloat() * 0.55f + 0.1f
            )

            _activeAnimals.value = remaining + newActive
        }
    }

    private fun updateAnimalPhase(instanceId: String, phase: AnimalPhase) {
        _activeAnimals.value = _activeAnimals.value.map {
            if (it.instanceId == instanceId) it.copy(phase = phase) else it
        }
    }

    private fun playSound(animalId: String) {
        soundMap[animalId]?.let { soundId ->
            if (loadedSounds.contains(soundId)) {
                soundPool.play(soundId, 1.0f, 1.0f, 1, 0, 1.0f)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        soundPool.release()
    }
}
