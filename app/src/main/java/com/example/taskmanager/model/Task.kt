package com.example.taskmanager.model

data class Task (
    val id: Long,
    val title: String,
    val isCompleted: Boolean =false
)
