package com.example.learnkotlin.repositories

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.learnkotlin.datebases.HabitTrackerDatabase
import com.example.learnkotlin.interfaces.IHabitElementDao
import com.example.learnkotlin.models.HabitElement
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class HabitElementRepository(application: Application) : IHabitElementDao {
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

    private val habitElementDao: IHabitElementDao =
        HabitTrackerDatabase.getInstance(application).habitElementDao()

    val habitElements: LiveData<List<HabitElement>> = habitElementDao.getAll()

    override fun getAll(): LiveData<List<HabitElement>> {
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

    override fun getById(id: Int): LiveData<HabitElement> {
        return habitElementDao.getById(id)
    }

    override suspend fun insert(habitElement: HabitElement) {
        withContext(Dispatchers.IO) { habitElementDao.insert(habitElement) }
    }

    override suspend fun update(habitElement: HabitElement) {
        withContext(Dispatchers.IO) { habitElementDao.update(habitElement) }
    }

    override suspend fun delete(habitElement: HabitElement) {
        withContext(Dispatchers.IO) { habitElementDao.delete(habitElement) }
    }

    override suspend fun deleteAll() {
        withContext(Dispatchers.IO) { habitElementDao.deleteAll() }
    }
}