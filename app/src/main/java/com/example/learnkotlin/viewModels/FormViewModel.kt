package com.example.learnkotlin.viewModels

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.learnkotlin.models.HabitElement
import com.example.learnkotlin.repositories.HabitElementRepository
import kotlinx.coroutines.*


class FormViewModel(
    application: Application, id: Int,
) : ViewModel(), CoroutineScope {
    private val habitElementRepository: HabitElementRepository =
        HabitElementRepository.getInstance(application)
    private val mutableHabitElement = MutableLiveData<HabitElement>()
    override val coroutineContext =
        Dispatchers.IO + CoroutineExceptionHandler { _, exception -> throw exception }

    init {
        habitElementRepository.getById(id).observeForever { mutableHabitElement.postValue(it) }
    }

    var habit = mutableHabitElement as LiveData<HabitElement?>

    @Synchronized
    private suspend fun updateHabit(habitElement: HabitElement) {
        withContext(Dispatchers.Main) {
            val newHabitElement = mutableHabitElement.value?.apply {
                this.title = habitElement.title
                this.title = habitElement.title
                this.description = habitElement.description
                this.type = habitElement.type
                this.priority = habitElement.priority
                this.color = habitElement.color
                this.completeCounter = habitElement.completeCounter
                this.periodNumber = habitElement.periodNumber
            } ?: habitElement
            mutableHabitElement.value = newHabitElement
        }
    }

    fun setHabit(habitElement: HabitElement) {
        launch {
            updateHabit(habitElement)
            mutableHabitElement.value?.let { habitElementRepository.update(it) }
        }
    }

    fun addHabit(habitElement: HabitElement) {
        launch {
            updateHabit(habitElement)
            mutableHabitElement.value?.let { habitElementRepository.insert(it) }
        }
    }

    fun deleteHabit(habitElement: HabitElement) {
        launch {
            updateHabit(habitElement)
            mutableHabitElement.value?.let { habitElementRepository.delete(it) }
        }
    }

    fun validateHabit(habitElement: HabitElement): Boolean {
        if (habitElement.title.isEmpty()) {
            return false
        }

        return true
    }
}