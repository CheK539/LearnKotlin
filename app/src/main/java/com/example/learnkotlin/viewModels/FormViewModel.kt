package com.example.learnkotlin.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.learnkotlin.models.HabitElement
import com.example.learnkotlin.repositories.HabitElementRepository

class FormViewModel(habitElement: HabitElement?) : ViewModel() {
    private val mutableHabitElement = MutableLiveData(habitElement)
    private val habitElementRepository: HabitElementRepository = HabitElementRepository.instance
    private val mutableHabits = habitElementRepository.habitElements

    var habit = mutableHabitElement as LiveData<HabitElement?>

    fun setHabit(habitElement: HabitElement) {
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
        mutableHabitElement.postValue(newHabitElement)
    }

    fun addHabit(habitElement: HabitElement) {
        mutableHabits.postValue(mutableHabits.value?.apply { this.add(habitElement) })
    }

    fun deleteHabit(habitElement: HabitElement) {
        mutableHabits.postValue(mutableHabits.value?.apply { this.remove(habitElement) })
    }

    fun validateHabit(habitElement: HabitElement): Boolean {
        if (habitElement.title.isEmpty()) {
            return false
        }

        return true
    }
}