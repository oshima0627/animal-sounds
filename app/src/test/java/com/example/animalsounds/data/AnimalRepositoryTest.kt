package com.example.animalsounds.data

import com.example.animalsounds.data.model.Animal
import org.junit.Assert.*
import org.junit.Test

class AnimalRepositoryTest {

    private val animals = AnimalRepository.animals

    // ── 件数・存在確認 ────────────────────────────────────────────────────────

    @Test
    fun animals_hasCorrectCount() {
        assertEquals("動物は14匹であること", 14, animals.size)
    }

    @Test
    fun animals_containsAllExpectedIds() {
        val ids = animals.map { it.id }.toSet()
        val expected = setOf(
            "dog", "cat", "horse", "cow", "chicken", "chick", "sheep", "goat",
            "lion", "elephant", "wolf", "boar", "crow", "sparrow"
        )
        assertEquals(expected, ids)
    }

    // ── 一意性 ───────────────────────────────────────────────────────────────

    @Test
    fun animals_haveUniqueIds() {
        val ids = animals.map { it.id }
        assertEquals("ID は重複しないこと", ids.distinct().size, ids.size)
    }

    // ── フィールドの非空チェック ──────────────────────────────────────────────

    @Test
    fun animals_allHaveNonEmptyNameJp() {
        animals.forEach { animal ->
            assertTrue("${animal.id}: nameJp が空", animal.nameJp.isNotEmpty())
        }
    }

    @Test
    fun animals_allHaveNonEmptySoundText() {
        animals.forEach { animal ->
            assertTrue("${animal.id}: soundText が空", animal.soundText.isNotEmpty())
        }
    }

    @Test
    fun animals_allHaveNonEmptyEmoji() {
        animals.forEach { animal ->
            assertTrue("${animal.id}: emoji が空", animal.emoji.isNotEmpty())
        }
    }

    // ── 命名規則チェック ─────────────────────────────────────────────────────

    @Test
    fun animals_soundResNames_followNamingConvention() {
        animals.forEach { animal ->
            assertTrue(
                "${animal.id}: soundResName は 'sound_' で始まること",
                animal.soundResName.startsWith("sound_")
            )
        }
    }

    @Test
    fun animals_soundResNames_containNoUpperCase() {
        animals.forEach { animal ->
            assertEquals(
                "${animal.id}: soundResName は小文字のみ（Androidリソース命名規則）",
                animal.soundResName.lowercase(),
                animal.soundResName
            )
        }
    }

    // ── 背景色チェック ────────────────────────────────────────────────────────

    @Test
    fun animals_backgroundColors_areFullyOpaque() {
        animals.forEach { animal ->
            val alpha = (animal.backgroundColor ushr 24) and 0xFF
            assertEquals(
                "${animal.id}: 背景色のアルファ値は 0xFF (不透明) であること",
                0xFF, alpha.toInt()
            )
        }
    }

    @Test
    fun animals_backgroundColors_areUnique() {
        val colors = animals.map { it.backgroundColor }
        assertEquals(
            "各動物の背景色は異なること（子供が区別しやすいように）",
            colors.distinct().size, colors.size
        )
    }
}
