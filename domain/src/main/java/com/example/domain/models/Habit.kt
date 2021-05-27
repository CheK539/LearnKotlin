package com.example.domain.models

import android.graphics.Color
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.domain.converters.DoneListConverter
import com.example.domain.converters.HabitTypeConverter
import com.example.domain.converters.PriorityConverter
import com.example.domain.enums.HabitType
import com.example.domain.enums.PriorityType

@TypeConverters(PriorityConverter::class, HabitTypeConverter::class, DoneListConverter::class)
@Entity(tableName = "habit_table")
data class Habit(
    var title: String,
    var description: String,
    var priority: PriorityType,
    var type: HabitType,
    var completeCounter: Int,
    var periodNumber: Int,
    var color: String,
) {
    @PrimaryKey
    var uid: String = ""

    var doneList: MutableList<Long> = mutableListOf()
    var date = 0L
    var doneCounter = 0
    var endPeriod = 0L

    fun toHabitNetwork(): HabitNetwork {
        return HabitNetwork(
            title,
            description,
            priority.priorityId,
            type.idType,
            completeCounter,
            periodNumber,
            Color.parseColor(color.split(" ")[0]),
            date,
            uid,
            doneList
        )
    }

    fun toHabitUid(): HabitUid {
        return HabitUid(uid)
    }

    fun toHabitDone(): HabitDone {
        return HabitDone(date, uid)
    }
}



