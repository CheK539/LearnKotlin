package com.example.learnkotlin.models

import android.os.Parcelable
import com.example.learnkotlin.enums.HabitType
import kotlinx.android.parcel.Parcelize

@Parcelize
data class HabitElement(
    var title: String,
    var description: String,
    var priority: String,
    var type: HabitType,
    var completeCounter: Int,
    var periodNumber: String,
    var color: String
) : Parcelable