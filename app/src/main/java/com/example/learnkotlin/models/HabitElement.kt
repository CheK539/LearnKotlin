package com.example.learnkotlin.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class HabitElement(
    var title: String,
    var description: String,
    var priority: String,
    var type: String,
    var completeCounter: Int,
    var periodNumber: String,
    var color: String
) : Parcelable