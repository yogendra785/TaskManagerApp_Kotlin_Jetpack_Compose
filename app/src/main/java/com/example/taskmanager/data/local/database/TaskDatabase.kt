package com.example.taskmanager.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.taskmanager.data.local.entity.TaskEntity
import com.example.taskmanager.data.dao.TaskDao

@Database(
    entities = [TaskEntity::class],
    version=1
)
abstract class TaskDatabase : RoomDatabase(){
    abstract fun taskDao(): TaskDao
}


