package com.example.learnkotlin.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.learnkotlin.enums.HabitType
import com.example.learnkotlin.models.HabitElement
import com.example.learnkotlin.repositories.HabitElementRepository
import java.util.*
import kotlin.collections.ArrayList

class HabitsViewModel : ViewModel() {
    private val habitElementRepository: HabitElementRepository = HabitElementRepository.instance
    private val mutableHabits = habitElementRepository.habitElements
    private val filteredHabits = MutableLiveData<ArrayList<HabitElement>>(mutableHabits.value)

    init {
        mutableHabits.observeForever { filteredHabits.postValue(mutableHabits.value) }
    }

    val habits: LiveData<ArrayList<HabitElement>> = filteredHabits

    fun getHabitsByType(habitType: HabitType): ArrayList<HabitElement> {
        val habits = filteredHabits.value ?: ArrayList()
        return ArrayList(habits.filter { habitElement -> habitElement.type == habitType })
    }

    fun filterHabits(text: String?) {
        if (text == null || text.isEmpty())
            filteredHabits.postValue(mutableHabits.value)
        else {
            val newFilteredHabits = ArrayList<HabitElement>()
            val filterPattern = text.toLowerCase(Locale.ROOT)

            mutableHabits.value?.forEach { habit ->
                if (habit.title.toLowerCase(Locale.ROOT).contains(filterPattern))
                    newFilteredHabits.add(habit)
            }

            filteredHabits.postValue(newFilteredHabits)
        }
    }

    fun sortedByPriority(isDescending: Boolean) {
        if (filteredHabits.value?.size == 0)
            return

        val newFilteredHabits = if (!isDescending) filteredHabits.value
            ?.sortedBy { it.priority.priorityId }
        else filteredHabits.value
            ?.sortedByDescending { it.priority.priorityId }

        filteredHabits.postValue(ArrayList(newFilteredHabits))
    }

    fun clearFilter() {
        filteredHabits.postValue(mutableHabits.value)
    }
}