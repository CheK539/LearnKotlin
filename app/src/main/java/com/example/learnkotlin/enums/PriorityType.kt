package com.example.learnkotlin.enums

enum class PriorityType(val priorityId: Int, val stringValue: String) {
    Low(0, "Low"),
    Medium(1, "Medium"),
    High(2, "High"),
    VeryHigh(3, "Very high");

    companion object {
        fun fromString(value: String) = values().first { it.stringValue == value }
    }
}