package com.example.data.interfaces

import com.example.domain.models.HabitDone
import com.example.domain.models.HabitNetwork
import com.example.domain.models.HabitUid
import retrofit2.Response
import retrofit2.http.*

interface HabitService {
    @GET("api/habit")
    suspend fun getHabits(): Response<List<HabitNetwork>>

    @PUT("api/habit")
    suspend fun addHabit(@Body habitElement: HabitNetwork): Response<HabitUid>

    @HTTP(method = "DELETE", path = "api/habit", hasBody = true)
    suspend fun deleteHabit(@Body habitUid: HabitUid): Response<Unit>

    @POST("api/habit_done")
    suspend fun doneHabit(@Body habitDone: HabitDone): Response<Unit>
}