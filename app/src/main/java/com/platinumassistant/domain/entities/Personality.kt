package com.platinumassistant.domain.entities

/**
 * Entity representing an AI Personality
 */
data class Personality(
    val id: String,
    val name: String,
    val description: String,
    val category: PersonalityCategory,
    val voiceId: String,
    val speed: Float = 1.0f,
    val pitch: Float = 1.0f,
    val characteristics: List<String> = emptyList(),
    val isAvailable: Boolean = true,
    val isFavorite: Boolean = false,
    val usageCount: Int = 0,
    val lastUsed: Long = 0L,
)

enum class PersonalityCategory {
    TECHNICAL,
    COMEDY,
    HEROES,
    ARABIC,
    HISTORICAL,
    CUSTOM
}
