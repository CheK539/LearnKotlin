package com.example.learnkotlin.interfaces

import com.example.learnkotlin.models.HabitElement
import com.example.learnkotlin.models.HabitElementUid
import retrofit2.Response
import retrofit2.http.*

interface IHabitService {
    @Headers("Authorization:c95d0e92-1c58-40b6-9df5-6ed542cb8233")

    @GET("api/habit")
    suspend fun getHabit(): Response<List<HabitElement>>

    @Headers("Authorization:c95d0e92-1c58-40b6-9df5-6ed542cb8233")
    @PUT("api/habit")
    suspend fun addHabit(@Body habitElement: HabitElement): Response<HabitElementUid>

    @Headers("Authorization:c95d0e92-1c58-40b6-9df5-6ed542cb8233")
    @HTTP(method = "DELETE", path = "api/habit", hasBody = true)
    suspend fun deleteHabit(@Body habitElementUid: HabitElementUid): Response<Unit>

    @POST("api/habit_done")
    suspend fun completeHabit(): Response<String>
}