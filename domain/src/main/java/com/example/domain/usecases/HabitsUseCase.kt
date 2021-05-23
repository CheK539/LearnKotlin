package com.example.domain.usecases

import com.example.domain.interfaces.HabitRepository
import com.example.domain.models.Habit
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class HabitsUseCase @Inject constructor(
    private val habitRepository: HabitRepository
) {
    fun getHabits(): Flow<List<Habit>> {
        return habitRepository.getAll()
    }

    fun getByTitle(title: String): Flow<List<Habit>> {
        return habitRepository.getByTitle(title)
    }

    fun getByPriorityAscending(): Flow<List<Habit>> {
        return habitRepository.getByPriorityAscending()
    }

    fun getByPriorityDescending(): Flow<List<Habit>> {
        return habitRepository.getByPriorityDescending()
    }
}