package com.example.data.repositories

import androidx.lifecycle.LiveData
import com.example.data.interfaces.HabitDao
import com.example.data.interfaces.HabitService
import com.example.domain.interfaces.HabitRepository
import com.example.domain.models.Habit
import com.example.domain.models.HabitNetwork
import com.example.domain.models.HabitUid
import com.example.data.network.RepeatRequester
import kotlinx.coroutines.*

class HabitRepositoryImpl(private val habitDao: HabitDao, private val habitService: HabitService) :
    HabitRepository, CoroutineScope {
    companion object {
        private var instance: HabitRepositoryImpl? = null

        fun getInstance(habitDao: HabitDao, habitService: HabitService): HabitRepositoryImpl {
            instance?.let {
                return it
            }

            instance = HabitRepositoryImpl(habitDao, habitService)

            return instance as HabitRepositoryImpl
        }
    }

    override val coroutineContext =
        Dispatchers.IO + CoroutineExceptionHandler { _, exception -> throw exception }

    override suspend fun getAll(): LiveData<List<Habit>> {
        withContext(Dispatchers.IO) {
            val response =
                RepeatRequester<List<HabitNetwork>>().getResponse { habitService.getHabits() }

            response?.let {
                it.body()?.forEach { habit -> habitDao.insert(habit.toHabitElement()) }
            }
        }

        return habitDao.getAll()
    }

    override fun getByTitle(title: String): LiveData<List<Habit>> {
        return habitDao.getByTitle(title)
    }

    override fun getByPriorityAscending(): LiveData<List<Habit>> {
        return habitDao.getByPriorityAscending()
    }

    override fun getByPriorityDescending(): LiveData<List<Habit>> {
        return habitDao.getByPriorityDescending()
    }

    override fun getByUid(uid: String): LiveData<Habit> {
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
}