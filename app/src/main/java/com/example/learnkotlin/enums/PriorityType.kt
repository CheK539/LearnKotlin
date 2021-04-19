package com.example.learnkotlin.enums

enum class PriorityType(priorityId: Int, stringValue: String) {
    Low(0, "Low"),
    Medium(1, "Medium"),
    High(2, "High"),
    VeryHigh(3, "Very high");

    companion object {
        fun fromString(value: String) = HabitType.values().first { it.typeString == value }
    }
}