package com.example.domain.usecases

import com.example.domain.interfaces.HabitRepository
import com.example.domain.models.Habit
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetByUidHabitsUseCase @Inject constructor(
    private val habitRepository: HabitRepository
) {
    fun getByUid(uid: String): Flow<Habit> {
        return habitRepository.getByUid(uid)
    }
}