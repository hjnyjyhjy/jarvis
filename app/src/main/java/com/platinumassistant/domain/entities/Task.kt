package com.platinumassistant.domain.entities

/**
 * Entity representing a Task/Todo item
 */
data class Task(
    val id: String,
    val title: String,
    val description: String = "",
    val dueDate: Long? = null,
    val priority: TaskPriority = TaskPriority.MEDIUM,
    val isCompleted: Boolean = false,
    val category: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val completedAt: Long? = null,
    val reminders: List<Long> = emptyList(),
)

enum class TaskPriority {
    LOW,
    MEDIUM,
    HIGH,
    URGENT
}
