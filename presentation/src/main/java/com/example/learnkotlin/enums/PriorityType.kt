package com.example.learnkotlin.enums

enum class PriorityType(val priorityId: Int, val stringValue: String) {
    Low(0, "Low"),
    Medium(1, "Medium"),
    High(2, "High");

    companion object {
        fun fromString(value: String) = values().first { it.stringValue == value }
        fun fromInt(value: Int) = values().first { it.priorityId == value }
    }
}