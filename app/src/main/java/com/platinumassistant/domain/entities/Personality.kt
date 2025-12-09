package com.platinumassistant.domain.entities

/**
 * Entity representing an AI Personality with full configuration
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
    val language: String = "ar",
    val keywords: List<String> = emptyList(),
    val imageUrl: String? = null,
    val systemPrompt: String = "You are a helpful assistant",
    val temperature: Float = 0.7f,
    val maxTokens: Int = 512,
    val enableMemory: Boolean = true,
    val memoryCap: Int = 100
) {
    fun canUse(): Boolean = isAvailable && !id.isEmpty()
}

enum class PersonalityCategory {
    TECHNICAL,          // Software engineers, hackers (Jarvis, Friday, Elon, etc.)
    COMEDY,            // Deadpool, Joker, SpongeBob, etc.
    HEROES,            // Batman, Superman, Wonder Woman, etc.
    ARABIC,            // Arab philosophers, poets, scholars
    HISTORICAL,        // Einstein, Newton, Tesla, etc.
    CUSTOM             // User-created personalities
}
