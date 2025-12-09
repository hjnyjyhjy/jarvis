package com.platinumassistant.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.platinumassistant.domain.entities.Task

/**
 * Room entity representing a task in the database
 * Maps domain Task entity to SQLCipher-encrypted storage
 */
@Entity(tableName = "tasks")
data class TaskEntity(
    @PrimaryKey val id: String,
    val title: String,
    val description: String,
    val category: String,
    val priority: String, // HIGH, MEDIUM, LOW
    val dueDate: Long,
    val isCompleted: Boolean = false,
    val completedAt: Long? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
    val reminders: String = "", // JSON string of reminder times
    val subtasks: String = "", // JSON string of subtask titles
    val tags: String = "", // Comma-separated tags
    val attachments: String = "", // JSON string of file paths
    val assignedPersonality: String? = null, // Personality ID for task-specific help
    val metadata: String = "" // Additional JSON metadata
) {
    
    fun toDomainModel(): Task {
        return Task(
            id = id,
            title = title,
            description = description,
            category = category,
            priority = priority,
            dueDate = dueDate,
            isCompleted = isCompleted,
            completedAt = completedAt,
            createdAt = createdAt,
            updatedAt = updatedAt,
            reminders = reminders.split(",").filter { it.isNotBlank() }.map { it.toLongOrNull() ?: 0L },
            subtasks = subtasks.split("||").filter { it.isNotBlank() },
            tags = tags.split(",").filter { it.isNotBlank() },
            attachments = attachments.split("||").filter { it.isNotBlank() },
            assignedPersonality = assignedPersonality,
            metadata = metadata
        )
    }
    
    companion object {
        fun fromDomainModel(task: Task): TaskEntity {
            return TaskEntity(
                id = task.id,
                title = task.title,
                description = task.description,
                category = task.category,
                priority = task.priority,
                dueDate = task.dueDate,
                isCompleted = task.isCompleted,
                completedAt = task.completedAt,
                createdAt = task.createdAt,
                updatedAt = task.updatedAt,
                reminders = task.reminders.joinToString(","),
                subtasks = task.subtasks.joinToString("||"),
                tags = task.tags.joinToString(","),
                attachments = task.attachments.joinToString("||"),
                assignedPersonality = task.assignedPersonality,
                metadata = task.metadata
            )
        }
    }
}
