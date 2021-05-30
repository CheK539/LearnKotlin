package com.example.data.datebases

import androidx.room.*
import androidx.room.migration.AutoMigrationSpec
import com.example.data.interfaces.HabitDao
import com.example.domain.models.Habit

@Database(
    entities = [Habit::class],
    version = 9,
    autoMigrations = [
        AutoMigration(
            from = 7,
            to = 8,
            spec = HabitTrackerDatabase.AutoMigration7To8::class
        ),
        AutoMigration(
            from = 8,
            to = 9,
            spec = HabitTrackerDatabase.AutoMigration8To9::class
        )]
)
abstract class HabitTrackerDatabase : RoomDatabase() {
    @DeleteColumn(tableName = "habit_table", columnName = "endPeriod")
    class AutoMigration7To8 : AutoMigrationSpec

    abstract fun habitDao(): HabitDao

    @DeleteColumn(tableName = "habit_table", columnName = "doneCounter")
    class AutoMigration8To9 : AutoMigrationSpec
}