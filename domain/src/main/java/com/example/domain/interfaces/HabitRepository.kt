package com.example.domain.interfaces

import com.example.domain.models.Habit
import kotlinx.coroutines.flow.Flow

interface HabitRepository {
    fun getAll(): Flow<List<Habit>>
    fun getByTitle(title: String): Flow<List<Habit>>
    fun getByPriorityAscending(): Flow<List<Habit>>
    fun getByPriorityDescending(): Flow<List<Habit>>
    fun getByUid(uid: String): Flow<Habit>
    suspend fun insert(habitElement: Habit)
    suspend fun update(habitElement: Habit)
    suspend fun delete(habitElement: Habit)
    suspend fun deleteAll()
}