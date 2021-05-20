package com.example.domain.enums

enum class HabitType(val idType: Int, val typeString: String) {
    Positive(0, "Positive"),
    Negative(1, "Negative");

    companion object {
        fun fromString(value: String) = values().first { it.typeString == value }
        fun fromId(value: Int) = values().first { it.idType == value }
    }
}