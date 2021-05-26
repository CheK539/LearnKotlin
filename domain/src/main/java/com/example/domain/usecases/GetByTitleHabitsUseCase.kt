package com.example.domain.usecases

import com.example.domain.interfaces.HabitRepository
import com.example.domain.models.Habit
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetByTitleHabitsUseCase @Inject constructor(
    private val habitRepository: HabitRepository
) {
    fun getByTitle(title: String): Flow<List<Habit>> {
        return habitRepository.getByTitle(title)
    }
}