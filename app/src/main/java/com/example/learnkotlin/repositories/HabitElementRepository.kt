package com.example.learnkotlin.repositories

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.learnkotlin.datebases.HabitTrackerDatabase
import com.example.learnkotlin.interfaces.IHabitElementDao
import com.example.learnkotlin.interfaces.IHabitService
import com.example.learnkotlin.models.HabitElement
import com.example.learnkotlin.models.HabitElementDeserializer
import com.example.learnkotlin.models.HabitElementSerializer
import com.example.learnkotlin.models.HabitElementUid
import com.google.gson.GsonBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

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

    private val habitService: IHabitService

    val habitElements: LiveData<List<HabitElement>> = habitElementDao.getAll()

    init {
        val gson = GsonBuilder()
            .registerTypeAdapter(HabitElement::class.java, HabitElementDeserializer())
            .registerTypeAdapter(HabitElement::class.java, HabitElementSerializer())
            .create()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://droid-test-server.doubletapp.ru/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        habitService = retrofit.create(IHabitService::class.java)
    }

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
        withContext(Dispatchers.IO) {
            try {
                val responseDefferend = async { habitService.addHabit(habitElement) }
                val response = responseDefferend.await()
                habitElement.uid = response.body()?.uid ?: habitElement.uid
                habitElementDao.update(habitElement)
            } catch (e: Exception) {
            }

            habitElementDao.insert(habitElement)
        }
    }

    override suspend fun update(habitElement: HabitElement) {
        withContext(Dispatchers.IO) {
            launch {
                try {
                    habitService.addHabit(habitElement)
                } catch (e: Exception) {
                }
            }

            habitElementDao.update(habitElement)
        }
    }

    override suspend fun delete(habitElement: HabitElement) {
        withContext(Dispatchers.IO) {
            launch {
                try {
                    habitService.deleteHabit(HabitElementUid(habitElement.uid))
                } catch (e: Exception) {
                }
            }
            habitElementDao.delete(habitElement)
        }
    }

    override suspend fun deleteAll() {
        withContext(Dispatchers.IO) { habitElementDao.deleteAll() }
    }
}