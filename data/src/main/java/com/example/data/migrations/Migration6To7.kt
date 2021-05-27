package com.example.data.migrations

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

object Migration6To7: Migration(6, 7) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("alter table habit_table add column doneList TEXT NOT NULL DEFAULT ''")
    }

}