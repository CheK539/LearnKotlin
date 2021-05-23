package com.example.domain.usecases

import com.example.domain.interfaces.HabitRepository
import com.example.domain.models.Habit
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FormUseCase @Inject constructor(
    private val habitRepository: HabitRepository,
    private val dispatcher: CoroutineDispatcher
) {
    suspend fun update(habit: Habit) {
        withContext(dispatcher) { habitRepository.update(habit) }
    }

    suspend fun add(habit: Habit) {
        withContext(dispatcher) { habitRepository.insert(habit) }
    }

    suspend fun delete(habit: Habit) {
        withContext(dispatcher) { habitRepository.delete(habit) }
    }

    fun getByUid(uid: String): Flow<Habit> {
       return habitRepository.getByUid(uid)
    }
}