package com.example.domain.usecases

import com.example.domain.interfaces.HabitRepository
import com.example.domain.models.Habit
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AddHabitsUseCase @Inject constructor(
    private val habitRepository: HabitRepository,
    private val dispatcher: CoroutineDispatcher
) {
    suspend fun add(habit: Habit) {
        withContext(dispatcher) { habitRepository.insert(habit) }
    }
}