package com.example.learnkotlin.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.learnkotlin.models.HabitElement
import com.example.learnkotlin.repositories.HabitElementRepository
import java.util.*
import kotlin.collections.ArrayList

class HabitsViewModel : ViewModel() {
    private var habitElementRepository: HabitElementRepository = HabitElementRepository.instance
    private var mutableHabits: MutableLiveData<ArrayList<HabitElement>> =
        habitElementRepository.habitElements
    private var filteredHabits: MutableLiveData<ArrayList<HabitElement>> =
        MutableLiveData(ArrayList(mutableHabits.value))

    fun addHabit(habitElement: HabitElement) {
        mutableHabits.postValue(mutableHabits.value?.apply { this.add(habitElement) })
        filteredHabits.postValue(mutableHabits.value)
    }

    fun deleteHabit(habitElement: HabitElement) {
        mutableHabits.postValue(mutableHabits.value?.apply { this.remove(habitElement) })
        filteredHabits.postValue(mutableHabits.value)
    }

    fun getHabits(): LiveData<ArrayList<HabitElement>> {
        return filteredHabits
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