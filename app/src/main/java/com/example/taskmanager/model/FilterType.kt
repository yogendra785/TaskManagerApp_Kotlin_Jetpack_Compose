package com.example.taskmanager.model

import java.util.logging.Filter

sealed class  FilterType{
    object All : FilterType()
    object Active : FilterType()
    object Completed: FilterType()
}