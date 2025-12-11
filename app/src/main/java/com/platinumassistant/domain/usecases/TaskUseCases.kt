package com.platinumassistant.domain.usecases

import com.platinumassistant.domain.entities.Task
import com.platinumassistant.domain.entities.TaskPriority
import com.platinumassistant.domain.repositories.TaskRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Use case for getting all tasks
 */
class GetAllTasksUseCase @Inject constructor(
    private val taskRepository: TaskRepository
) {
    operator fun invoke(): Flow<List<Task>> {
        return taskRepository.getAllTasks()
    }
}

/**
 * Use case for getting pending (incomplete) tasks
 */
class GetPendingTasksUseCase @Inject constructor(
    private val taskRepository: TaskRepository
) {
    operator fun invoke(): Flow<List<Task>> {
        return taskRepository.getPendingTasks()
    }
}

/**
 * Use case for getting tasks by priority
 */
class GetTasksByPriorityUseCase @Inject constructor(
    private val taskRepository: TaskRepository
) {
    operator fun invoke(priority: String): Flow<List<Task>> {
        return taskRepository.getTasksByPriority(priority)
    }
}

/**
 * Use case for getting overdue tasks
 */
class GetOverdueTasksUseCase @Inject constructor(
    private val taskRepository: TaskRepository
) {
    operator fun invoke(): Flow<List<Task>> {
        return taskRepository.getOverdueTasks()
    }
}

/**
 * Use case for adding a task
 */
class AddTaskUseCase @Inject constructor(
    private val taskRepository: TaskRepository
) {
    suspend operator fun invoke(task: Task) {
        taskRepository.addTask(task)
    }
}

/**
 * Use case for updating a task
 */
class UpdateTaskUseCase @Inject constructor(
    private val taskRepository: TaskRepository
) {
    suspend operator fun invoke(task: Task) {
        taskRepository.updateTask(task)
    }
}

/**
 * Use case for completing a task
 */
class CompleteTaskUseCase @Inject constructor(
    private val taskRepository: TaskRepository
) {
    suspend operator fun invoke(taskId: String) {
        taskRepository.completeTask(taskId)
    }
}

/**
 * Use case for deleting a task
 */
class DeleteTaskUseCase @Inject constructor(
    private val taskRepository: TaskRepository
) {
    suspend operator fun invoke(taskId: String) {
        taskRepository.deleteTask(taskId)
    }
}

/**
 * Use case for clearing all completed tasks
 */
class ClearCompletedTasksUseCase @Inject constructor(
    private val taskRepository: TaskRepository
) {
    suspend operator fun invoke() {
        taskRepository.clearCompletedTasks()
    }
}
