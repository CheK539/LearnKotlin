package com.example.data.migrations

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

object Migration3To4 : Migration(3, 4) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL(
            "CREATE TABLE habit_table_new (uid TEXT NOT NULL, title TEXT NOT NULL, description TEXT NOT NULL," +
                    " priority INTEGER NOT NULL, type TEXT NOT NULL, completeCounter INTEGER NOT NULL, periodNumber INTEGER NOT NULL," +
                    " color TEXT NOT NULL, date INTEGER NOT NULL" +
                    ", PRIMARY KEY(uid))"
        )

        database.execSQL(
            "INSERT INTO habit_table_new (uid, title, description, priority, type," +
                    " completeCounter, periodNumber, color, date)" +
                    " SELECT CAST(id as TEXT), title, description, priority, type, completeCounter," +
                    " CAST(periodNumber as INTEGER), color, date FROM habit_table"
        )

        database.execSQL("DROP TABLE habit_table")

        database.execSQL("ALTER TABLE habit_table_new RENAME TO habit_table")
    }
}