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
    val availability: String = "always", // when personality is available
    val costPerMessage: Float = 0f, // For future premium features
    val metadata: String = "" // Additional JSON metadata
) {
    
    fun toDomainModel(): Personality {
        return Personality(
            id = id,
            name = name,
            description = description,
            category = category,
            avatar = avatar,
            voiceId = voiceId,
            voiceSpeed = voiceSpeed,
            voicePitch = voicePitch,
            language = language,
            systemPrompt = systemPrompt,
            temperature = temperature,
            maxTokens = maxTokens,
            topK = topK,
            topP = topP,
            memorySize = memorySize,
            memoryStrategy = memoryStrategy,
            isFavorite = isFavorite,
            isSelected = isSelected,
            usageCount = usageCount,
            lastUsed = lastUsed,
            createdAt = createdAt,
            updatedAt = updatedAt,
            tags = tags.split(",").filter { it.isNotBlank() },
            traits = traits,
            availability = availability,
            costPerMessage = costPerMessage,
            metadata = metadata
        )
    }
    
    companion object {
        fun fromDomainModel(personality: Personality): PersonalityEntity {
            return PersonalityEntity(
                id = personality.id,
                name = personality.name,
                description = personality.description,
                category = personality.category,
                avatar = personality.avatar,
                voiceId = personality.voiceId,
                voiceSpeed = personality.voiceSpeed,
                voicePitch = personality.voicePitch,
                language = personality.language,
                systemPrompt = personality.systemPrompt,
                temperature = personality.temperature,
                maxTokens = personality.maxTokens,
                topK = personality.topK,
                topP = personality.topP,
                memorySize = personality.memorySize,
                memoryStrategy = personality.memoryStrategy,
                isFavorite = personality.isFavorite,
                isSelected = personality.isSelected,
                usageCount = personality.usageCount,
                lastUsed = personality.lastUsed,
                createdAt = personality.createdAt,
                updatedAt = personality.updatedAt,
                tags = personality.tags.joinToString(","),
                traits = personality.traits,
                availability = personality.availability,
                costPerMessage = personality.costPerMessage,
                metadata = personality.metadata
            )
        }
    }
}
