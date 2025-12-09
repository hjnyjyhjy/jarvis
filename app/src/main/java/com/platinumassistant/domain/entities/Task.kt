package com.platinumassistant.domain.entities

import java.util.UUID

/**
 * Entity representing a Task/Todo item with full tracking
 */
data class Task(
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val description: String = "",
    val dueDate: Long? = null,
    val priority: TaskPriority = TaskPriority.MEDIUM,
    val isCompleted: Boolean = false,
    val category: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val completedAt: Long? = null,
    val reminders: List<Long> = emptyList(),
    val location: String? = null,
    val tags: List<String> = emptyList(),
    val subtasks: List<String> = emptyList(),
    val assignee: String? = null,
    val progress: Int = 0
) {
    fun isOverdue(): Boolean = !isCompleted && dueDate != null && dueDate < System.currentTimeMillis()
    fun getDaysUntilDue(): Int? = dueDate?.let { ((it - System.currentTimeMillis()) / (1000 * 60 * 60 * 24)).toInt() }
    fun complete() = this.copy(isCompleted = true, completedAt = System.currentTimeMillis())
}

enum class TaskPriority {
    LOW,        // Non-urgent
    MEDIUM,     // Normal priority
    HIGH,       // Important
    URGENT      // Must do today
}
