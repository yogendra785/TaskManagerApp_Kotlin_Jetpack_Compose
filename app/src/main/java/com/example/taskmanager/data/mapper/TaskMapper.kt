package com.example.taskmanager.data.mapper

import com.example.taskmanager.data.local.entity.TaskEntity
import com.example.taskmanager.model.Task

fun TaskEntity.toTask(): Task {
    return Task(
        id = id,
        title = title,
        isCompleted = isCompleted
    )
}
