package com.example.data.builders

import com.example.data.interceptors.AuthorizationInterceptor
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitBuilder {
    fun build(): Retrofit {
        val gson = GsonBuilder().create()
        val logging = HttpLoggingInterceptor().apply { setLevel(HttpLoggingInterceptor.Level.BODY) }
        val okHttpClient = OkHttpClient().newBuilder()
            .addInterceptor(logging)
            .addInterceptor(AuthorizationInterceptor)
            .build()

        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl("https://droid-test-server.doubletapp.ru/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }
}