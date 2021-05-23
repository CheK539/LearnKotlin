package com.example.data.interfaces

import androidx.room.*
import com.example.domain.models.Habit
import kotlinx.coroutines.flow.Flow


@Dao
interface HabitDao {
    @Query("select * from habit_table")
    fun getAll(): Flow<List<Habit>>

    @Query("select * from habit_table where title like :title")
    fun getByTitle(title: String): Flow<List<Habit>>

    @Query("select * from habit_table order by priority asc")
    fun getByPriorityAscending(): Flow<List<Habit>>

    @Query("select * from habit_table order by priority desc")
    fun getByPriorityDescending(): Flow<List<Habit>>

    @Query("select * from habit_table where uid like :uid limit 1")
    fun getByUid(uid: String): Flow<Habit>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(habitElement: Habit)

    @Update
    suspend fun update(habitElement: Habit)

    @Delete
    suspend fun delete(habitElement: Habit)

    @Query("delete from habit_table ")
    suspend fun deleteAll()
}