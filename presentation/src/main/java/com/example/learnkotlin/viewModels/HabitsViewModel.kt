package com.example.learnkotlin.viewModels

import androidx.lifecycle.*
import com.example.domain.enums.HabitType
import com.example.domain.models.Habit
import com.example.domain.usecases.HabitsUseCase
import kotlinx.coroutines.launch

class HabitsViewModel(private val habitsUseCase: HabitsUseCase) : ViewModel() {
    /*private val habitElementRepository: HabitRepositoryImpl =
        HabitRepositoryImpl.getInstance(application)*/
    private val mutableHabits = MutableLiveData<List<Habit>>()
    private val filteredHabits = MutableLiveData<List<Habit>>()

    private var searchHabits: LiveData<List<Habit>>? = null
    private var sortedHabits: LiveData<List<Habit>>? = null
    private val observer: Observer<List<Habit>> = Observer<List<Habit>> {
        filteredHabits.postValue(it)
    }

    init {
        viewModelScope.launch {
            habitsUseCase.getHabits().observeForever { mutableHabits.postValue(it) }
            mutableHabits.observeForever(observer)
        }
        //habitElementRepository.habitElements.observeForever(observer)
    }

    val habits: LiveData<List<Habit>> = filteredHabits

    fun getHabitsByType(habitType: HabitType): List<Habit> {
        return filteredHabits.value?.filter { habitElement -> habitElement.type == habitType }
            ?: listOf()
    }

    fun searchHabits(text: String?) {
        if (text == null || text.isEmpty())
            filteredHabits.postValue(mutableHabits.value)
        else {
            searchHabits?.removeObserver(observer)
            searchHabits = habitsUseCase.getByTitle("%$text%")
            searchHabits?.observeForever(observer)
        }
    }

    fun sortedByPriority(isDescending: Boolean) {
        if (isDescending) {
            sortedHabits?.removeObserver(observer)
            sortedHabits = habitsUseCase.getByPriorityDescending()
            sortedHabits?.observeForever(observer)
        } else {
            sortedHabits?.removeObserver(observer)
            sortedHabits = habitsUseCase.getByPriorityAscending()
            sortedHabits?.observeForever(observer)
        }
    }

    fun clearFilter() {
        filteredHabits.postValue(mutableHabits.value)
    }
}