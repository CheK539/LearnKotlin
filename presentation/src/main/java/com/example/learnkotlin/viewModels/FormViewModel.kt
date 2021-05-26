package com.example.learnkotlin.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.domain.models.Habit
import com.example.domain.usecases.*
import kotlinx.coroutines.*
import java.util.*


class FormViewModel(
    private val addHabitsUseCase: AddHabitsUseCase,
    private val updateHabitsUseCase: UpdateHabitsUseCase,
    private val deleteHabitsUseCase: DeleteHabitsUseCase,
    private val getByUidHabitsUseCase: GetByUidHabitsUseCase,
    uid: String?
) : ViewModel(), CoroutineScope {
    private val mutableHabitElement = MutableLiveData<Habit>()
    override val coroutineContext =
        Dispatchers.IO + CoroutineExceptionHandler { _, exception -> throw exception }

    init {
        uid?.let { uidKey ->
            getByUidHabitsUseCase.getByUid(uidKey).asLiveData()
                .observeForever { mutableHabitElement.postValue(it) }
        }
    }

    var habit = mutableHabitElement as LiveData<Habit?>

    private suspend fun updateHabit(habitElement: Habit) {
        withContext(Dispatchers.Main) {
            val time = Calendar.getInstance().timeInMillis
            habitElement.date = time

            val newHabitElement = mutableHabitElement.value?.apply {
                this.title = habitElement.title
                this.title = habitElement.title
                this.description = habitElement.description
                this.type = habitElement.type
                this.priority = habitElement.priority
                this.color = habitElement.color
                this.completeCounter = habitElement.completeCounter
                this.periodNumber = habitElement.periodNumber
                this.date = habitElement.date
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