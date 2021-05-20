package com.example.domain.usecases

import androidx.lifecycle.LiveData
import com.example.domain.interfaces.HabitRepository
import com.example.domain.models.Habit
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class HabitsUseCase(
    private val habitRepository: HabitRepository,
    private val dispatcher: CoroutineDispatcher
) {
    suspend fun getHabits(): LiveData<List<Habit>> {
        return withContext(dispatcher) { habitRepository.getAll() }
    }

    fun getByTitle(title: String): LiveData<List<Habit>> {
        return habitRepository.getByTitle(title)
    }

    fun getByPriorityAscending(): LiveData<List<Habit>> {
        return habitRepository.getByPriorityAscending()
    }

    fun getByPriorityDescending(): LiveData<List<Habit>> {
        return habitRepository.getByPriorityDescending()
    }
}