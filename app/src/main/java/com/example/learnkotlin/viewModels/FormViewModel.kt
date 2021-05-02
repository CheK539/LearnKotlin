package com.example.learnkotlin.viewModels

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.learnkotlin.models.HabitElement
import com.example.learnkotlin.repositories.HabitElementRepository


class FormViewModel(application: Application, id: Int) : ViewModel() {
    private val habitElementRepository: HabitElementRepository = HabitElementRepository.getInstance(application)
    private val mutableHabitElement = MutableLiveData<HabitElement>()

    init {
        habitElementRepository.getById(id).observeForever { mutableHabitElement.postValue(it) }
    }

    var habit = mutableHabitElement as LiveData<HabitElement?>

    private fun updateHabit(habitElement: HabitElement) {
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

    fun setHabit(habitElement: HabitElement) {
        updateHabit(habitElement)
        mutableHabitElement.value?.let { habitElementRepository.update(it) }
    }

    fun addHabit(habitElement: HabitElement) {
        updateHabit(habitElement)
        mutableHabitElement.value?.let { habitElementRepository.insert(it) }
    }

    fun deleteHabit(habitElement: HabitElement) {
        updateHabit(habitElement)
        mutableHabitElement.value?.let { habitElementRepository.delete(it) }
    }

    fun validateHabit(habitElement: HabitElement): Boolean {
        if (habitElement.title.isEmpty()) {
            return false
        }

        return true
    }
}