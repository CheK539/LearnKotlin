package com.example.learnkotlin.converters

import androidx.room.TypeConverter
import com.example.learnkotlin.enums.HabitType

class HabitTypeConverter {
    @TypeConverter
    fun fromHabitType(habitType: HabitType): String {
        return habitType.typeString
    }

    @TypeConverter
    fun toHabitType(data: String): HabitType {
        return HabitType.fromString(data)
    }
}