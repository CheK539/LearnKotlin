package com.example.learnkotlin.interfaces

import com.example.learnkotlin.models.HabitElement

interface IDisplayFormCallback {
    fun addHabit(habitElement: HabitElement)
    fun replaceHabit(habitElement: HabitElement, position: Int)
    fun deleteHabit(position: Int)
}