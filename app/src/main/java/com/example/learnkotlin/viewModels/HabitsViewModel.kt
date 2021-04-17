package com.example.learnkotlin.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.learnkotlin.models.HabitElement
import com.example.learnkotlin.repositories.HabitElementRepository

class HabitsViewModel : ViewModel() {
    private var habitElementRepository: HabitElementRepository = HabitElementRepository.instance
    private var mutableHabits: MutableLiveData<ArrayList<HabitElement>> =
        habitElementRepository.habitElements

    fun addHabit(habitElement: HabitElement) {
        mutableHabits.postValue(mutableHabits.value?.apply { this.add(habitElement) })
    }

    fun deleteHabit(habitElement: HabitElement) {
        mutableHabits.postValue(mutableHabits.value?.apply { this.remove(habitElement) })
    }

    fun getHabits(): LiveData<ArrayList<HabitElement>> {
        return mutableHabits
    }
}