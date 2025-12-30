package com.example.taskmanager.data.local.entity

import androidx.room.Entity

import androidx.room.PrimaryKey
@Entity(tableName= "tasks")
data class TaskEntity(
    @PrimaryKey
    val id: Long,
    val title: String,
    val isCompleted: Boolean

)
