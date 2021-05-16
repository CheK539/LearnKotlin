package com.example.learnkotlin.models

import android.graphics.Color
import com.example.learnkotlin.enums.HabitType
import com.example.learnkotlin.enums.PriorityType


data class HabitNetwork(
    val title: String,
    val description: String,
    val priority: Int,
    val type: Int,
    val count: Int,
    val frequency: Int,
    val color: Int,
    val date: Long,
    val uid: String
) {
    fun toHabitElement(): HabitElement {
        val hsv = FloatArray(3)
        Color.colorToHSV(color, hsv)
        val hexColor = "#${"%x".format(color)}"
        val stringColor = "$hexColor ${hsv[0]} ${hsv[1]} ${hsv[2]}"

        return HabitElement(
            title,
            description,
            PriorityType.fromInt(priority),
            HabitType.fromId(type),
            count,
            frequency,
            stringColor
        ).apply {
            date = this@HabitNetwork.date
            uid = this@HabitNetwork.uid
        }
    }
}