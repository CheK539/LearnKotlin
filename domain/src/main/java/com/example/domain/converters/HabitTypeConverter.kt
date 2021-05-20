package com.example.domain.converters

import androidx.room.TypeConverter
import com.example.domain.enums.HabitType

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