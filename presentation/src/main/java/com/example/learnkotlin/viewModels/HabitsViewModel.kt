package com.example.learnkotlin.viewModels

import androidx.lifecycle.*
import androidx.lifecycle.Observer
import com.example.domain.enums.HabitType
import com.example.domain.models.Habit
import com.example.domain.usecases.*
import com.example.learnkotlin.scopes.ViewModelScope
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject


@ViewModelScope
class HabitsViewModel @Inject constructor(
    getHabitsUseCase: GetHabitsUseCase,
    private val getByTitleHabitsUseCase: GetByTitleHabitsUseCase,
    private val getByPriorityAscendingHabitsUseCase: GetByPriorityAscendingHabitsUseCase,
    private val getByPriorityDescendingHabitsUseCase: GetByPriorityDescendingHabitsUseCase,
    private val updateHabitsUseCase: UpdateHabitsUseCase,
    private val doneHabitUseCase: DoneHabitUseCase,
) : ViewModel() {
    private var mutableHabits: MutableLiveData<List<Habit>> = MutableLiveData()
    private val filteredHabits = MutableLiveData<List<Habit>>()

    private var searchHabits: LiveData<List<Habit>>? = null
    var sortedHabits: LiveData<List<Habit>>? = null
    private val observer: Observer<List<Habit>> = Observer<List<Habit>> {
        filteredHabits.postValue(it)
    }

    init {
        getHabitsUseCase.getHabits().asLiveData().observeForever { mutableHabits.postValue(it) }
        mutableHabits.observeForever(observer)
    }

    val habits: LiveData<List<Habit>> = filteredHabits

    fun getHabitsByType(habitType: HabitType): List<Habit> {
        return filteredHabits.value?.filter { habitElement -> habitElement.type == habitType }
            ?: listOf()
    }

    fun searchHabits(text: String?) {
        if (text == null || text.isEmpty()) {
            searchHabits?.removeObserver(observer)
            filteredHabits.postValue(mutableHabits.value)
        } else {
            searchHabits?.removeObserver(observer)
            searchHabits = getByTitleHabitsUseCase.getByTitle("%$text%").asLiveData()
            searchHabits?.observeForever(observer)
        }
    }

    fun sortedByPriority(isDescending: Boolean) {
        if (isDescending) {
            sortedHabits?.removeObserver(observer)
            sortedHabits = getByPriorityDescendingHabitsUseCase
                .getByPriorityDescending()
                .asLiveData()
            sortedHabits?.observeForever(observer)
        } else {
            sortedHabits?.removeObserver(observer)
            sortedHabits = getByPriorityAscendingHabitsUseCase
                .getByPriorityAscending()
                .asLiveData()
            sortedHabits?.observeForever(observer)
        }
    }

    fun clearFilter() {
        sortedHabits?.removeObserver(observer)
        sortedHabits = null
        searchHabits?.removeObserver(observer)
        filteredHabits.postValue(mutableHabits.value)
    }

    fun increaseDoneCounter(uid: String): Int {
        val habit = mutableHabits.value?.firstOrNull { it.uid == uid }
        var difference = 0

        habit?.let {
            difference = it.completeCounter - it.doneList.size - 1

            if (difference >= 0) {
                val date = Calendar.getInstance().timeInMillis
                it.doneList.add(date)
                it.date = date

                viewModelScope.launch {
                    updateHabitsUseCase.update(it)
                    doneHabitUseCase.doneHabit(it)
                }
            }
        }

        return difference
    }
}