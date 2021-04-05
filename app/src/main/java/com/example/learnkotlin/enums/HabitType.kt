package com.example.learnkotlin.enums

enum class HabitType(val typeString: String) {
    Positive("Positive"),
    Negative("Negative");

    companion object {
        fun fromString(value: String) = values().first { it.typeString == value }
    }
}