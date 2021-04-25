package com.example.learnkotlin.models

import android.os.Parcelable
import androidx.room.*
import com.example.learnkotlin.enums.HabitType
import com.example.learnkotlin.enums.PriorityType
import kotlinx.android.parcel.Parcelize

@Parcelize
@TypeConverters(PriorityConverter::class, HabitTypeConverter::class)
@Entity(tableName = "habit_table")
data class HabitElement(
    @ColumnInfo var title: String,
    var description: String,
    var priority: PriorityType,
    var type: HabitType,
    var completeCounter: Int,
    var periodNumber: String,
    var color: String
) : Parcelable {
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null
}

class PriorityConverter {
    @TypeConverter
    fun fromPriority(priority: PriorityType): String {
        return priority.stringValue
    }

    @TypeConverter
    fun toPriority(data: String): PriorityType {
        return PriorityType.fromString(data)
    }
}

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