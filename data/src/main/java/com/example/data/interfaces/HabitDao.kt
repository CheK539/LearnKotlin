package com.example.data.interfaces

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.domain.models.Habit

@Dao
interface HabitDao {
    @Query("select * from habit_table")
    fun getAll(): LiveData<List<Habit>>

    @Query("select * from habit_table where title like :title")
    fun getByTitle(title: String): LiveData<List<Habit>>

    @Query("select * from habit_table order by priority asc")
    fun getByPriorityAscending(): LiveData<List<Habit>>

    @Query("select * from habit_table order by priority desc")
    fun getByPriorityDescending(): LiveData<List<Habit>>

    @Query("select * from habit_table where uid like :uid limit 1")
    fun getByUid(uid: String): LiveData<Habit>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(habitElement: Habit)

    @Update
    suspend fun update(habitElement: Habit)

    @Delete
    suspend fun delete(habitElement: Habit)

    @Query("delete from habit_table ")
    suspend fun deleteAll()
}