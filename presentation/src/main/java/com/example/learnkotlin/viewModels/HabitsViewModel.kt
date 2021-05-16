package com.example.learnkotlin.viewModels

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.example.learnkotlin.enums.HabitType
import com.example.learnkotlin.models.HabitElement
import com.example.learnkotlin.repositories.HabitElementRepository

class HabitsViewModel(application: Application) : ViewModel() {
    private val habitElementRepository: HabitElementRepository =
        HabitElementRepository.getInstance(application)
    private val filteredHabits =
        MutableLiveData<List<HabitElement>>(habitElementRepository.habitElements.value)

    private var searchHabits: LiveData<List<HabitElement>>? = null
    private var sortedHabits: LiveData<List<HabitElement>>? = null
    private val observer: Observer<List<HabitElement>> = Observer<List<HabitElement>> {
        filteredHabits.postValue(it)
    }

    init {
        habitElementRepository.habitElements.observeForever(observer)
    }

    val habits: LiveData<List<HabitElement>> = filteredHabits

    fun getHabitsByType(habitType: HabitType): List<HabitElement> {
        return filteredHabits.value?.filter { habitElement -> habitElement.type == habitType }
            ?: listOf()
    }

    fun searchHabits(text: String?) {
        if (text == null || text.isEmpty())
            filteredHabits.postValue(habitElementRepository.habitElements.value)
        else {
            searchHabits?.removeObserver(observer)
            searchHabits = habitElementRepository.getByTitle("%$text%")
            searchHabits?.observeForever(observer)
        }
    }

    fun sortedByPriority(isDescending: Boolean) {
        if (isDescending) {
            sortedHabits?.removeObserver(observer)
            sortedHabits = habitElementRepository.getByPriorityDescending()
            sortedHabits?.observeForever(observer)
        } else {
            sortedHabits?.removeObserver(observer)
            sortedHabits = habitElementRepository.getByPriorityAscending()
            sortedHabits?.observeForever(observer)
        }
    }

    fun clearFilter() {
        filteredHabits.postValue(habitElementRepository.habitElements.value)
    }
}