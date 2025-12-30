package com.example.taskmanager.data.repository

import com.example.taskmanager.data.dao.TaskDao
import com.example.taskmanager.data.local.entity.TaskEntity
import kotlinx.coroutines.flow.Flow


class TaskRepository(
    private val taskDao : TaskDao
){
    fun getAllTasks():Flow<List<TaskEntity>>{
        return taskDao.getAllTasks()

    }

    suspend fun insertTask(task: TaskEntity){
        taskDao.insertTask(task)
    }
    suspend fun deleteTask(task: TaskEntity){
        taskDao.deleteTask(task)
    }

    suspend fun updateTaskStatus(id:Long,completed:Boolean){
        taskDao.updateTaskStatus(id,completed)
    }
}