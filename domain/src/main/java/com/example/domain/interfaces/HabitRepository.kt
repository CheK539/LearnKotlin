package com.example.domain.interfaces

import androidx.lifecycle.LiveData
import com.example.domain.models.Habit

interface HabitRepository {
    suspend fun getAll(): LiveData<List<Habit>>
    fun getByTitle(title: String): LiveData<List<Habit>>
    fun getByPriorityAscending(): LiveData<List<Habit>>
    fun getByPriorityDescending(): LiveData<List<Habit>>
    fun getByUid(uid: String): LiveData<Habit>
    suspend fun insert(habitElement: Habit)
    suspend fun update(habitElement: Habit)
    suspend fun delete(habitElement: Habit)
    suspend fun deleteAll()
}