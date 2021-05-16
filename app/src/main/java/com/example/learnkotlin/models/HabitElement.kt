package com.example.learnkotlin.models

import android.graphics.Color
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.learnkotlin.converters.HabitTypeConverter
import com.example.learnkotlin.converters.PriorityConverter
import com.example.learnkotlin.enums.HabitType
import com.example.learnkotlin.enums.PriorityType

@TypeConverters(PriorityConverter::class, HabitTypeConverter::class)
@Entity(tableName = "habit_table")
data class HabitElement(
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

    var date = 0L

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
            uid
        )
    }

    fun toHabitUid(): HabitUid {
        return HabitUid(uid)
    }
}



