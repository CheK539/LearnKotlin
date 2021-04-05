package com.example.learnkotlin.interfaces

import com.example.learnkotlin.models.HabitElement

interface IDisplayFormCallback {
    fun addHabit(habitElement: HabitElement)
    fun replaceHabit(oldHabitElement: HabitElement, newHabitElement: HabitElement)
    fun deleteHabit(habitElement: HabitElement)
}