package com.example.taskmanager.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.room.Query
import com.example.taskmanager.data.local.entity.TaskEntity
import com.example.taskmanager.data.mapper.toTask
import com.example.taskmanager.model.FilterType
import com.example.taskmanager.model.Task
import com.example.taskmanager.data.repository.TaskRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class TaskViewModel(
    private val repository: TaskRepository
) : ViewModel() {

    // -----------------------------
    // 1️⃣ FILTER STATE
    // -----------------------------
    private val _filter = MutableStateFlow<FilterType>(FilterType.All)
    val filter: StateFlow<FilterType> = _filter

    //search state
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    //snackbar for undo

    private var recentlyDeletedTask: Task? = null

    fun updateSearch(query: String){
        _searchQuery.value = query
    }

    fun setFilter(filterType: FilterType) {
        _filter.value = filterType
    }

    // -----------------------------
    // 2️⃣ SOURCE OF TRUTH (ALL TASKS)
    // -----------------------------
    private val allTasks: StateFlow<List<Task>> =
        repository.getAllTasks() // Flow<List<TaskEntity>>
            .map { entities ->
                entities.map { it.toTask() } // Entity → Domain
            }
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5_000),
                emptyList()
            )

    // -----------------------------
    // 3️⃣ FILTERED TASKS (USED BY UI)
    // -----------------------------
    val tasks: StateFlow<List<Task>> =
        combine(allTasks, _filter, _searchQuery) { tasks, filter, query ->

            val filtered = when (filter) {
                FilterType.All -> tasks
                FilterType.Active -> tasks.filter { !it.isCompleted }
                FilterType.Completed -> tasks.filter { it.isCompleted }
            }

            if (query.isBlank()) {
                filtered
            } else {
                filtered.filter {
                    it.title.contains(query, ignoreCase = true)
                }
            }

        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            emptyList()
        )


    // -----------------------------
    // 4️⃣ ACTIONS
    // -----------------------------
    fun addTask(title: String) {
        viewModelScope.launch {
            repository.insertTask(
                TaskEntity(
                    id = System.currentTimeMillis(),
                    title = title,
                    isCompleted = false
                )
            )
        }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch {
            recentlyDeletedTask = task
            repository.deleteTask(
                TaskEntity(
                    id = task.id,
                    title=task.title,
                    isCompleted = task.isCompleted
                )
            )
        }
    }

    //undo delete
    fun undoDelete(){
        recentlyDeletedTask?.let { task ->
            viewModelScope.launch {
                repository.insertTask(
                    TaskEntity(
                        id=task.id,
                        title=task.title,
                        isCompleted = task.isCompleted
                    )
                )
                recentlyDeletedTask=null
            }
        }
    }

    fun toggleTask(task: Task) {
        viewModelScope.launch {
            repository.updateTaskStatus(
                task.id,
                !task.isCompleted
            )
        }
    }
}


class TaskViewModelFactory(
    private val repository: TaskRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TaskViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TaskViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

