package com.example.learnkotlin.interfaces

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import com.example.learnkotlin.models.HabitElement

@Dao
interface IHabitElementDao {
    @Query("select * from habit_table")
    fun getAll(): LiveData<List<HabitElement>>

    @Query("select * from habit_table where title like :title")
    fun getByTitle(title: String): LiveData<List<HabitElement>>

    @Query("select * from habit_table order by priority asc")
    fun getByPriorityAscending(): LiveData<List<HabitElement>>

    @Query("select * from habit_table order by priority desc")
    fun getByPriorityDescending(): LiveData<List<HabitElement>>

    @Query("select * from habit_table where uid like :uid limit 1")
    fun getByUid(uid: String): LiveData<HabitElement>

    @Insert(onConflict = REPLACE)
    suspend fun insert(habitElement: HabitElement)

    @Update
    suspend fun update(habitElement: HabitElement)

    @Delete
    suspend fun delete(habitElement: HabitElement)

    @Query("delete from habit_table ")
    suspend fun deleteAll()
}