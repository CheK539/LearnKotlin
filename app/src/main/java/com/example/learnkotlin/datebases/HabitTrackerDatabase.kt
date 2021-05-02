package com.example.learnkotlin.datebases

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.learnkotlin.interfaces.IHabitElementDao
import com.example.learnkotlin.models.HabitElement

@Database(entities = [HabitElement::class], version = 1)
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
            ).build()

            return instance as HabitTrackerDatabase
        }
    }

    abstract fun habitElementDao(): IHabitElementDao
}