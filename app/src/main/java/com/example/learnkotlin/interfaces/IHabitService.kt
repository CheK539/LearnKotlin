package com.example.learnkotlin.interfaces

import com.example.learnkotlin.models.HabitNetwork
import com.example.learnkotlin.models.HabitUid
import retrofit2.Response
import retrofit2.http.*

interface IHabitService {
    @GET("api/habit")
    suspend fun getHabits(): Response<List<HabitNetwork>>

    @PUT("api/habit")
    suspend fun addHabit(@Body habitElement: HabitNetwork): Response<HabitUid>

    @HTTP(method = "DELETE", path = "api/habit", hasBody = true)
    suspend fun deleteHabit(@Body habitUid: HabitUid): Response<Unit>

    @POST("api/habit_done")
    suspend fun completeHabit(): Response<String>
}