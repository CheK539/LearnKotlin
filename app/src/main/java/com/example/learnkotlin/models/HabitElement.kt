package com.example.learnkotlin.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.learnkotlin.converters.HabitTypeConverter
import com.example.learnkotlin.converters.PriorityConverter
import com.example.learnkotlin.enums.HabitType
import com.example.learnkotlin.enums.PriorityType
import kotlinx.android.parcel.Parcelize

@Parcelize
@TypeConverters(PriorityConverter::class, HabitTypeConverter::class)
@Entity(tableName = "habit_table")
data class HabitElement(
    var title: String,
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



