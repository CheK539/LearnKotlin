package com.example.data.datebases

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.interfaces.HabitDao
import com.example.data.migrations.*
import com.example.domain.models.Habit

@Database(entities = [Habit::class], version = 7)
abstract class HabitTrackerDatabase : RoomDatabase() {
    companion object {
        private var instance: HabitTrackerDatabase? = null

        fun getInstance(context: Context): HabitTrackerDatabase {
            instance?.let {
                return it
            }

            instance = Room.databaseBuilder(
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

            return instance as HabitTrackerDatabase
        }
    }

    abstract fun habitDao(): HabitDao
}