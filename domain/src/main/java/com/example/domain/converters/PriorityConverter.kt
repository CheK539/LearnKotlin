package com.example.domain.converters

import androidx.room.TypeConverter
import com.example.domain.enums.PriorityType

class PriorityConverter {
    @TypeConverter
    fun fromPriority(priority: PriorityType): Int {
        return priority.priorityId
    }

    @TypeConverter
    fun toPriority(data: Int): PriorityType {
        return PriorityType.fromInt(data)
    }
}