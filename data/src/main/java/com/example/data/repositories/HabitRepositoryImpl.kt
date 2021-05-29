package com.example.data.repositories

import com.example.data.interfaces.HabitDao
import com.example.data.interfaces.HabitService
import com.example.data.network.RepeatRequester
import com.example.domain.interfaces.HabitRepository
import com.example.domain.models.Habit
import com.example.domain.models.HabitNetwork
import com.example.domain.models.HabitUid
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow

class HabitRepositoryImpl(private val habitDao: HabitDao, private val habitService: HabitService) :
    HabitRepository, CoroutineScope {
    override val coroutineContext =
        Dispatchers.IO + CoroutineExceptionHandler { _, exception -> throw exception }

    override fun getAll(): Flow<List<Habit>> {
        launch { uploadFromNetwork() }

        return habitDao.getAll()
    }

    private suspend fun uploadFromNetwork() {
        withContext(Dispatchers.IO) {
            val response =
                RepeatRequester<List<HabitNetwork>>().getResponse { habitService.getHabits() }

            response?.let {
                it.body()?.forEach { habit -> habitDao.insert(habit.toHabitElement()) }
            }
        }
    }

    override fun getByTitle(title: String): Flow<List<Habit>> {
        return habitDao.getByTitle(title)
    }

    override fun getByPriorityAscending(): Flow<List<Habit>> {
        return habitDao.getByPriorityAscending()
    }

    override fun getByPriorityDescending(): Flow<List<Habit>> {
        return habitDao.getByPriorityDescending()
    }

    override fun getByUid(uid: String): Flow<Habit> {
        return habitDao.getByUid(uid)
    }

    override suspend fun insert(habitElement: Habit) {
        withContext(Dispatchers.IO) {
            val response = RepeatRequester<HabitUid>().getResponse {
                habitService.addHabit(habitElement.toHabitNetwork())
            }
            response?.let { habitElement.uid = it.body()?.uid ?: habitElement.uid }
            habitDao.insert(habitElement)
        }
    }

    override suspend fun update(habitElement: Habit) {
        withContext(Dispatchers.IO) {
            launch {
                RepeatRequester<HabitUid>().getResponse {
                    habitService.addHabit(habitElement.toHabitNetwork())
                }
            }

            habitDao.update(habitElement)
        }
    }

    override suspend fun delete(habitElement: Habit) {
        withContext(Dispatchers.IO) {
            launch {
                RepeatRequester<Unit>().getResponse {
                    habitService.deleteHabit(habitElement.toHabitUid())
                }
            }

            habitDao.delete(habitElement)
        }
    }

    override suspend fun deleteAll() {
        withContext(Dispatchers.IO) { habitDao.deleteAll() }
    }

    override suspend fun doneHabit(habitElement: Habit) {
        withContext(Dispatchers.IO) {
            habitService.doneHabit(habitElement.toHabitDone())
        }
    }
}