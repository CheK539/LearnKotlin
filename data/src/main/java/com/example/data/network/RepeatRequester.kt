package com.example.data.network

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import retrofit2.Response

class RepeatRequester<T>(private val repeatCount: Int = 3) {
    suspend fun getResponse(
        serviceFunction: suspend () -> Response<T>
    ): Response<T>? {
        return withContext(Dispatchers.IO) {
            var tryCount = 0

            while (tryCount < repeatCount) {
                try {
                    return@withContext serviceFunction()

                } catch (e: Exception) {
                    Log.d("Connection failed", "Failed ${e.message} - try $tryCount")
                    delay(3000)
                }

                tryCount++
            }
            return@withContext null
        }

    }
}