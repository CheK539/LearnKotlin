package com.example.data.builders

import android.content.Context
import androidx.room.Room
import com.example.data.datebases.HabitTrackerDatabase
import com.example.data.migrations.*

class HabitTrackerDataBaseBuilder(private val context: Context) {
    fun build(): HabitTrackerDatabase =
        Room.databaseBuilder(
            context,
            HabitTrackerDatabase::class.java,
            "habit_tracker_database"
        )
            .addMigrations(
                Migration1To2,
                Migration2To3,
                Migration3To4,
                Migration4To5,
                Migration5To6,
                Migration6To7
            )
            .build()
}