package com.example.domain.usecases

import com.example.domain.interfaces.HabitRepository
import com.example.domain.models.Habit
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetByPriorityAscendingHabitsUseCase @Inject constructor(
    private val habitRepository: HabitRepository
) {
    fun getByPriorityAscending(): Flow<List<Habit>> {
        return habitRepository.getByPriorityAscending()
    }
}