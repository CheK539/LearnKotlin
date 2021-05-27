package com.example.domain.converters

import androidx.room.TypeConverter

class DoneListConverter {
    @TypeConverter
    fun fromDoneList(doneList: MutableList<Long>): String {
        return doneList.joinToString(",")
    }

    @TypeConverter
    fun toDoneList(data: String): MutableList<Long> {
        return data.split(",").filter { it.isNotEmpty() }.map { it.toLong() }.toMutableList()
    }
}