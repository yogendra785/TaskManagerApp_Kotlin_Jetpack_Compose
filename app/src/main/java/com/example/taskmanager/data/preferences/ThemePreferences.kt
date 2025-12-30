package com.example.taskmanager.data.preferences

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


private val Context.dataStore by preferencesDataStore(("settings"))

class ThemePreferences (private val context:Context){
    private val DARK_MODE = booleanPreferencesKey("dark_mode")

    val isDarkMode: Flow<Boolean> = context.dataStore.data
        .map { prefs ->
            prefs[DARK_MODE] ?: false
        }
    suspend fun saveTheme(isDark : Boolean){
        context.dataStore.edit { prefs ->
            prefs[DARK_MODE]= isDark
        }
    }
}