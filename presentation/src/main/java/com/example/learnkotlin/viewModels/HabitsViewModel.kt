package com.example.learnkotlin.viewModels

import androidx.lifecycle.*
import androidx.lifecycle.Observer
import com.example.domain.enums.HabitType
import com.example.domain.models.Habit
import com.example.domain.usecases.FormUseCase
import com.example.domain.usecases.HabitsUseCase
import com.example.learnkotlin.components.FragmentScope
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@FragmentScope
class HabitsViewModel @Inject constructor(private val habitsUseCase: HabitsUseCase) : ViewModel() {
    private var mutableHabits: MutableLiveData<List<Habit>> = MutableLiveData()
    private val filteredHabits = MutableLiveData<List<Habit>>()

    private var searchHabits: LiveData<List<Habit>>? = null
    var sortedHabits: LiveData<List<Habit>>? = null
    private val observer: Observer<List<Habit>> = Observer<List<Habit>> {
        filteredHabits.postValue(it)
    }

    init {
        habitsUseCase.getHabits().asLiveData().observeForever { mutableHabits.postValue(it) }
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
            searchHabits = habitsUseCase.getByTitle("%$text%").asLiveData()
            searchHabits?.observeForever(observer)
        }
    }

    fun sortedByPriority(isDescending: Boolean) {
        if (isDescending) {
            sortedHabits?.removeObserver(observer)
            sortedHabits = habitsUseCase.getByPriorityDescending().asLiveData()
            sortedHabits?.observeForever(observer)
        } else {
            sortedHabits?.removeObserver(observer)
            sortedHabits = habitsUseCase.getByPriorityAscending().asLiveData()
            sortedHabits?.observeForever(observer)
        }
    }

    fun clearFilter() {
        sortedHabits?.removeObserver(observer)
        sortedHabits = null
        searchHabits?.removeObserver(observer)
        filteredHabits.postValue(mutableHabits.value)
    }

    fun increaseDoneCounter(uid: String, formUseCase: FormUseCase): String {
        val habit = mutableHabits.value?.firstOrNull { it.uid == uid }

        var message = ""
        habit?.let {
            val difference = it.completeCounter - it.doneCounter - 1

            if (difference > 0) {
                it.doneCounter++
                it.date = Calendar.getInstance().timeInMillis

                message = if (it.type == HabitType.Positive)
                    "Стоит выполнить еще $difference раза"
                else
                    "Можете выполнить еще $difference раз"

                viewModelScope.launch { formUseCase.update(it) }
            } else {
                message = if (it.type == HabitType.Positive)
                    "You are breathtaking!"
                else
                    "Хватит это делать"
            }
        }

        return message
    }
}