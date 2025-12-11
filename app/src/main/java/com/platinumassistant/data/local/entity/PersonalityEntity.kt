package com.platinumassistant.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.platinumassistant.domain.entities.Personality

/**
 * Room entity representing a personality in the database
 * Maps domain Personality entity to SQLCipher-encrypted storage
 */
@Entity(tableName = "personalities")
data class PersonalityEntity(
    @PrimaryKey val id: String,
    val name: String,
    val description: String,
    val category: String,
    val avatar: String, // File path or URI
    val voiceId: String,
    val voiceSpeed: Float = 1.0f,
    val voicePitch: Float = 1.0f,
    val language: String = "en",
    val systemPrompt: String,
    val temperature: Float = 0.7f,
    val maxTokens: Int = 2048,
    val topK: Int = 40,
    val topP: Float = 0.9f,
    val memorySize: Int = 10,
    val memoryStrategy: String = "sliding_window", // Context window type
    val isFavorite: Boolean = false,
    val isSelected: Boolean = false,
    val usageCount: Int = 0,
    val lastUsed: Long? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
    val tags: String = "", // Comma-separated tags
    val traits: String = "", // JSON string of personality traits
    val availability: String = "always", // Always available - completely FREE
    val metadata: String = "" // Additional JSON metadata
) {
    
    fun toDomainModel(): Personality {
        // Map storage representation into domain model, providing safe defaults
        val categoryEnum = try {
            PersonalityCategory.valueOf(category)
        } catch (e: Exception) {
            PersonalityCategory.CUSTOM
        }

        val keywordsList = tags.split(",").map { it.trim() }.filter { it.isNotBlank() }

        val characteristicsList = try {
            // traits may be a JSON or comma-separated string; attempt simple split
            traits.split(",").map { it.trim() }.filter { it.isNotBlank() }
        } catch (e: Exception) {
            emptyList()
        }

        return Personality(
            id = id,
            name = name,
            description = description,
            category = categoryEnum,
            voiceId = voiceId,
            speed = voiceSpeed,
            pitch = voicePitch,
            characteristics = characteristicsList,
            isAvailable = availability == "always" || availability == "available",
            isFavorite = isFavorite,
            usageCount = usageCount,
            lastUsed = lastUsed ?: 0L,
            language = language,
            keywords = keywordsList,
            imageUrl = if (avatar.isBlank()) null else avatar,
            systemPrompt = systemPrompt,
            temperature = temperature,
            maxTokens = maxTokens.coerceAtMost(4096),
            enableMemory = memorySize > 0,
            memoryCap = memorySize
        )
    }
    
    companion object {
        fun fromDomainModel(personality: Personality): PersonalityEntity {
            return PersonalityEntity(
                id = personality.id,
                name = personality.name,
                description = personality.description,
                category = personality.category.name,
                avatar = personality.imageUrl ?: "",
                voiceId = personality.voiceId,
                voiceSpeed = personality.speed,
                voicePitch = personality.pitch,
                language = personality.language,
                systemPrompt = personality.systemPrompt,
                temperature = personality.temperature,
                maxTokens = personality.maxTokens,
                topK = 40,
                topP = 0.9f,
                memorySize = if (personality.enableMemory) personality.memoryCap else 0,
                memoryStrategy = "sliding_window",
                isFavorite = personality.isFavorite,
                isSelected = false,
                usageCount = personality.usageCount,
                lastUsed = personality.lastUsed,
                createdAt = personality.createdAt,
                updatedAt = System.currentTimeMillis(),
                tags = personality.keywords.joinToString(","),
                traits = personality.characteristics.joinToString(","),
                availability = if (personality.isAvailable) "available" else "unavailable",
                metadata = ""
            )
        }
    }
}
