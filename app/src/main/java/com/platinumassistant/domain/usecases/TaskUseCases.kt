package com.platinumassistant.domain.usecases

import com.platinumassistant.domain.entities.Task
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
 * Use case for getting pending tasks
 */
class GetPendingTasksUseCase @Inject constructor(
    private val taskRepository: TaskRepository
) {
    operator fun invoke(): Flow<List<Task>> {
        return taskRepository.getPendingTasks()
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
 * Use case for completing a task
 */
class CompleteTaskUseCase @Inject constructor(
    private val taskRepository: TaskRepository
) {
    suspend operator fun invoke(taskId: String) {
        taskRepository.completeTask(taskId)
    }
}
