package com.example.taskmanager.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskmanager.data.preferences.ThemePreferences
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.SharingStarted

class ThemeViewModel (
    private val prefs: ThemePreferences
) : ViewModel(){
    val isDarkTheme : StateFlow<Boolean> =
            prefs.isDarkMode.stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(500),
                false
            )

    fun toggleTheme(){
        viewModelScope.launch {
            prefs.saveTheme(!isDarkTheme.value)
        }
    }
}