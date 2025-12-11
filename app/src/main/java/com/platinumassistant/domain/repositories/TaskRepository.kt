package com.platinumassistant.domain.repositories

import com.platinumassistant.domain.entities.Task
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for task/todo data access and persistence
 */
interface TaskRepository {
    
    fun getAllTasks(): Flow<List<Task>>
    
    fun getTasksByCategory(category: String): Flow<List<Task>>
    
    fun getTasksByPriority(priority: String): Flow<List<Task>>
    
    fun getPendingTasks(): Flow<List<Task>>
    
    fun getCompletedTasks(): Flow<List<Task>>
    
    fun getOverdueTasks(): Flow<List<Task>>
    
    suspend fun addTask(task: Task)
    
    suspend fun updateTask(task: Task)
    
    suspend fun deleteTask(id: String)
    
    suspend fun completeTask(id: String)
    
    suspend fun clearCompletedTasks()
    
    suspend fun getTaskById(id: String): Task?
    
    suspend fun searchTasks(query: String): List<Task>
}
