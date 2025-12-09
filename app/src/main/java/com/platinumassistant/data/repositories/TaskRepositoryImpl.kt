package com.platinumassistant.data.repositories

import com.platinumassistant.data.local.dao.TaskDao
import com.platinumassistant.data.local.entity.TaskEntity
import com.platinumassistant.domain.entities.Task
import com.platinumassistant.domain.repositories.TaskRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Implementation of TaskRepository
 * Handles all task-related data operations including CRUD and filtering
 */
class TaskRepositoryImpl @Inject constructor(
    private val taskDao: TaskDao
) : TaskRepository {
    
    override fun getAllTasks(): Flow<List<Task>> =
        taskDao.getAllTasks().map { entities ->
            entities.map { it.toDomainModel() }
        }
    
    override fun getPendingTasks(): Flow<List<Task>> =
        taskDao.getPendingTasks().map { entities ->
            entities.map { it.toDomainModel() }
        }
    
    override fun getTasksByPriority(priority: String): Flow<List<Task>> =
        taskDao.getTasksByPriority(priority).map { entities ->
            entities.map { it.toDomainModel() }
        }
    
    override fun getOverdueTasks(): Flow<List<Task>> =
        taskDao.getOverdueTasks(System.currentTimeMillis()).map { entities ->
            entities.map { it.toDomainModel() }
        }
    
    override suspend fun getTaskById(id: String): Task? =
        taskDao.getTaskById(id)?.toDomainModel()
    
    override suspend fun addTask(task: Task) {
        taskDao.insertTask(TaskEntity.fromDomainModel(task))
    }
    
    override suspend fun updateTask(task: Task) {
        taskDao.updateTask(TaskEntity.fromDomainModel(task))
    }
    
    override suspend fun completeTask(id: String): Boolean = try {
        val task = taskDao.getTaskById(id)
        if (task != null) {
            val completed = task.copy(
                isCompleted = true,
                completedAt = System.currentTimeMillis(),
                updatedAt = System.currentTimeMillis()
            )
            taskDao.updateTask(completed)
            true
        } else {
            false
        }
    } catch (e: Exception) {
        false
    }
    
    override suspend fun deleteTask(id: String): Boolean = try {
        val task = taskDao.getTaskById(id)
        if (task != null) {
            taskDao.deleteTask(task)
            true
        } else {
            false
        }
    } catch (e: Exception) {
        false
    }
    
    override suspend fun clearCompletedTasks() {
        taskDao.clearCompletedTasks()
    }
    
    override fun getTasksByCategory(category: String): Flow<List<Task>> =
        taskDao.getTasksByCategory(category).map { entities ->
            entities.map { it.toDomainModel() }
        }
    
    override suspend fun getPendingTaskCount(): Int =
        taskDao.getPendingTaskCount()
    
    override fun getCompletedTasks(): Flow<List<Task>> =
        taskDao.getCompletedTasks().map { entities ->
            entities.map { it.toDomainModel() }
        }
    
    override suspend fun clearAllTasks() {
        taskDao.clearAllTasks()
    }
}
