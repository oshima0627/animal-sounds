package com.example.animalsounds.data.model

import org.junit.Assert.*
import org.junit.Test

class AnimalTest {

    private val sampleDog = Animal(
        id = "dog",
        nameJp = "いぬ",
        soundText = "ワンワン！",
        emoji = "🐕",
        backgroundColor = 0xFFFFD7A8,
        soundResName = "sound_dog"
    )

    // ── データクラス基本動作 ──────────────────────────────────────────────────

    @Test
    fun animal_copy_producesEqualObject() {
        val copy = sampleDog.copy()
        assertEquals(sampleDog, copy)
        assertNotSame(sampleDog, copy)
    }

    @Test
    fun animal_copy_withDifferentId_isNotEqual() {
        val other = sampleDog.copy(id = "cat")
        assertNotEquals(sampleDog, other)
    }

    @Test
    fun animal_toString_containsId() {
        assertTrue(sampleDog.toString().contains("dog"))
    }

    // ── フィールド値の正確性 ─────────────────────────────────────────────────

    @Test
    fun animal_fieldsMatch_constructorArgs() {
        assertEquals("dog", sampleDog.id)
        assertEquals("いぬ", sampleDog.nameJp)
        assertEquals("ワンワン！", sampleDog.soundText)
        assertEquals("🐕", sampleDog.emoji)
        assertEquals(0xFFFFD7A8, sampleDog.backgroundColor)
        assertEquals("sound_dog", sampleDog.soundResName)
    }

    // ── hashCode 一貫性 ───────────────────────────────────────────────────────

    @Test
    fun animal_equalObjects_haveSameHashCode() {
        val copy = sampleDog.copy()
        assertEquals(sampleDog.hashCode(), copy.hashCode())
    }
}
