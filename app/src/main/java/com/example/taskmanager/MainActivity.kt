package com.example.taskmanager

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.room.Room
import com.example.taskmanager.data.local.database.TaskDatabase
import com.example.taskmanager.data.repository.TaskRepository
import com.example.taskmanager.screens.TaskScreen
import com.example.taskmanager.ui.theme.TaskManagerTheme
import com.example.taskmanager.viewmodel.TaskViewModel
import com.example.taskmanager.viewmodel.TaskViewModelFactory
import com.example.taskmanager.viewmodel.ThemeViewModel
import com.example.taskmanager.viewmodel.ThemeViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Build Room database and repository once
        val database = Room.databaseBuilder(
            applicationContext,
            TaskDatabase::class.java,
            "task_db"
        ).build()

        val repository = TaskRepository(database.taskDao())

        setContent {
            // Theme ViewModel
            val themeViewModel: ThemeViewModel = viewModel(
                factory = ThemeViewModelFactory(applicationContext)
            )
            val isDark = themeViewModel.isDarkTheme.collectAsState(initial = false).value

            // Task ViewModel
            val taskViewModel: TaskViewModel = viewModel(
                factory = TaskViewModelFactory(repository)
            )

            // Apply theme and show screen
            TaskManagerTheme(darkTheme = isDark) {
                TaskScreen(viewModel = taskViewModel,
                    themeViewModel = themeViewModel)
            }
        }
    }
}