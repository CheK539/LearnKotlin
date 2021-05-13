package com.example.learnkotlin.repositories

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.learnkotlin.datebases.HabitTrackerDatabase
import com.example.learnkotlin.interfaces.IHabitElementDao
import com.example.learnkotlin.interfaces.IHabitService
import com.example.learnkotlin.models.HabitElement
import com.example.learnkotlin.models.HabitElementUid
import com.example.learnkotlin.network.RepeatRequester
import com.example.learnkotlin.network.RetrofitNetwork
import kotlinx.coroutines.*

class HabitElementRepository(application: Application) : IHabitElementDao, CoroutineScope {
    companion object {
        private var instance: HabitElementRepository? = null

        fun getInstance(application: Application): HabitElementRepository {
            instance?.let {
                return it
            }

            instance = HabitElementRepository(application)

            return instance as HabitElementRepository
        }
    }

    override val coroutineContext =
        Dispatchers.IO + CoroutineExceptionHandler { _, exception -> throw exception }

    private val habitElementDao: IHabitElementDao =
        HabitTrackerDatabase.getInstance(application).habitElementDao()
    private val retrofitNetwork = RetrofitNetwork.getInstance()
    private val habitService = retrofitNetwork.retrofit.create(IHabitService::class.java)

    val habitElements: LiveData<List<HabitElement>> = getAll()

    override fun getAll(): LiveData<List<HabitElement>> {
        launch {
            val response =
                RepeatRequester<List<HabitElement>>().getResponse { habitService.getHabits() }

            response?.let {
                val habits = response.body()
                habits?.forEach { habitElementDao.insert(it) }
            }
        }

        return habitElementDao.getAll()
    }

    override fun getByTitle(title: String): LiveData<List<HabitElement>> {
        return habitElementDao.getByTitle(title)
    }

    override fun getByPriorityAscending(): LiveData<List<HabitElement>> {
        return habitElementDao.getByPriorityAscending()
    }

    override fun getByPriorityDescending(): LiveData<List<HabitElement>> {
        return habitElementDao.getByPriorityDescending()
    }

    override fun getByUid(uid: String): LiveData<HabitElement> {
        return habitElementDao.getByUid(uid)
    }

    override suspend fun insert(habitElement: HabitElement) {
        withContext(Dispatchers.IO) {
            val response = RepeatRequester<HabitElementUid>().getResponse {
                habitService.addHabit(habitElement)
            }
            response?.let { habitElement.uid = response.body()?.uid ?: habitElement.uid }
            habitElementDao.insert(habitElement)
        }
    }

    override suspend fun update(habitElement: HabitElement) {
        withContext(Dispatchers.IO) {
            launch {
                RepeatRequester<HabitElementUid>().getResponse { habitService.addHabit(habitElement) }
            }

            habitElementDao.update(habitElement)
        }
    }

    override suspend fun delete(habitElement: HabitElement) {
        withContext(Dispatchers.IO) {
            launch {
                RepeatRequester<Unit>().getResponse {
                    habitService.deleteHabit(HabitElementUid(habitElement.uid))
                }
            }

            habitElementDao.delete(habitElement)
        }
    }

    override suspend fun deleteAll() {
        withContext(Dispatchers.IO) { habitElementDao.deleteAll() }
    }
}