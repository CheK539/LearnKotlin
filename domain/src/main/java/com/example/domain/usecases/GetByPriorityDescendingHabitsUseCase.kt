package com.example.domain.usecases

import com.example.domain.interfaces.HabitRepository
import com.example.domain.models.Habit
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetByPriorityDescendingHabitsUseCase @Inject constructor(
    private val habitRepository: HabitRepository
) {
    fun getByPriorityDescending(): Flow<List<Habit>> {
        return habitRepository.getByPriorityDescending()
    }
}