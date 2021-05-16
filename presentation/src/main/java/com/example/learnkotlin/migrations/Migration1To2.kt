package com.example.learnkotlin.migrations

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

object Migration1To2 : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("alter table habit_table add column uid TEXT NOT NULL DEFAULT ''")
    }
}