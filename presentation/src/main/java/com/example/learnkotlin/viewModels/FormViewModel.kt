package com.example.learnkotlin.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.domain.models.Habit
import com.example.domain.usecases.AddHabitsUseCase
import com.example.domain.usecases.DeleteHabitsUseCase
import com.example.domain.usecases.GetByUidHabitsUseCase
import com.example.domain.usecases.UpdateHabitsUseCase
import kotlinx.coroutines.*
import java.util.*
import javax.inject.Inject


class FormViewModel @Inject constructor(
    private val addHabitsUseCase: AddHabitsUseCase,
    private val updateHabitsUseCase: UpdateHabitsUseCase,
    private val deleteHabitsUseCase: DeleteHabitsUseCase,
    private val getByUidHabitsUseCase: GetByUidHabitsUseCase
) : ViewModel(), CoroutineScope {
    private val mutableHabitElement = MutableLiveData<Habit>()
    override val coroutineContext =
        Dispatchers.IO + CoroutineExceptionHandler { _, exception -> throw exception }

    var habit = mutableHabitElement as LiveData<Habit?>

    fun loadByUid(uid: String?) {
        uid?.let { uidKey ->
            getByUidHabitsUseCase.getByUid(uidKey).asLiveData()
                .observeForever { mutableHabitElement.postValue(it) }
        }
    }

    private suspend fun updateHabit(habitElement: Habit) {
        withContext(Dispatchers.Main) {
            habitElement.apply {
                date = Calendar.getInstance().timeInMillis
            }

            val newHabitElement = mutableHabitElement.value?.apply {
                title = habitElement.title
                description = habitElement.description
                type = habitElement.type
                priority = habitElement.priority
                color = habitElement.color
                completeCounter = habitElement.completeCounter
                date = habitElement.date
                periodNumber = habitElement.periodNumber
            } ?: habitElement
            mutableHabitElement.value = newHabitElement
        }
    }

    fun setHabit(habitElement: Habit) {
        launch {
            updateHabit(habitElement)
            mutableHabitElement.value?.let { updateHabitsUseCase.update(it) }
        }
    }

    fun addHabit(habitElement: Habit) {
        launch {
            updateHabit(habitElement)
            mutableHabitElement.value?.let { addHabitsUseCase.add(it) }
        }
    }

    fun deleteHabit(habitElement: Habit) {
        launch {
            updateHabit(habitElement)
            mutableHabitElement.value?.let { deleteHabitsUseCase.delete(it) }
        }
    }

    fun validateHabit(habitElement: Habit): Boolean {
        return !(habitElement.title.isEmpty() || habitElement.description.isEmpty())
    }
}