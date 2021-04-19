package com.example.learnkotlin.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.learnkotlin.interfaces.IDisplayFormCallback
import com.example.learnkotlin.models.HabitElement

class FormViewModel(habitElement: HabitElement?, private val callback: IDisplayFormCallback?) : ViewModel() {
    private val mutableHabitElement = MutableLiveData(habitElement)

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
        callback?.habitChanged()
    }

    fun addHabit(habitElement: HabitElement) {
        callback?.addHabit(habitElement)
    }

    fun deleteHabit(habitElement: HabitElement) {
        callback?.deleteHabit(habitElement)
    }

    fun validateHabit(habitElement: HabitElement): Boolean {
        if (habitElement.title.isEmpty()) {
            return false
        }

        return true
    }
}