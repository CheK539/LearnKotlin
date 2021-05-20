package com.example.data.network

import com.example.data.interceptors.AuthorizationInterceptor
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitNetwork {
    companion object {
        private var instance: RetrofitNetwork? = null

        fun getInstance(): RetrofitNetwork {
            instance?.let {
                return it
            }

            instance = RetrofitNetwork()

            return instance as RetrofitNetwork
        }
    }

    val retrofit: Retrofit

    init {
        val gson = GsonBuilder().create()

        val logging = HttpLoggingInterceptor().apply { setLevel(HttpLoggingInterceptor.Level.BODY) }

        val okHttpClient = OkHttpClient().newBuilder()
            .addInterceptor(logging)
            .addInterceptor(AuthorizationInterceptor)
            .build()

        retrofit = Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl("https://droid-test-server.doubletapp.ru/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }
}