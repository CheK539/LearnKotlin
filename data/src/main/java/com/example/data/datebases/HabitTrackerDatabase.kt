package com.example.data.datebases

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.data.interfaces.HabitDao
import com.example.domain.models.Habit

@Database(entities = [Habit::class], version = 7)
abstract class HabitTrackerDatabase : RoomDatabase() {
    abstract fun habitDao(): HabitDao
}