package com.example.learnkotlin.interfaces

import com.example.learnkotlin.models.HabitElement
import com.example.learnkotlin.models.HabitElementUid
import retrofit2.Response
import retrofit2.http.*

interface IHabitService {
    @GET("api/habit")
    suspend fun getHabits(): Response<List<HabitElement>>

    @PUT("api/habit")
    suspend fun addHabit(@Body habitElement: HabitElement): Response<HabitElementUid>

    @HTTP(method = "DELETE", path = "api/habit", hasBody = true)
    suspend fun deleteHabit(@Body habitElementUid: HabitElementUid): Response<Unit>

    @POST("api/habit_done")
    suspend fun completeHabit(): Response<String>
}