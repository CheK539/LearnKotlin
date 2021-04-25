package com.example.learnkotlin.repositories

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.learnkotlin.HabitTrackerDatabase
import com.example.learnkotlin.interfaces.IHabitElementDao
import com.example.learnkotlin.models.HabitElement

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

    override fun insert(habitElement: HabitElement) {
        habitElementDao.insert(habitElement)
    }

    override fun update(habitElement: HabitElement) {
        habitElementDao.update(habitElement)
    }

    override fun delete(habitElement: HabitElement) {
        habitElementDao.delete(habitElement)
    }

    override fun deleteAll() {
        habitElementDao.deleteAll()
    }
}