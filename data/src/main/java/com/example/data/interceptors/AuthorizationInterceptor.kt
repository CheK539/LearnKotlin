package com.example.data.interceptors

import okhttp3.Interceptor
import okhttp3.Response

object AuthorizationInterceptor : Interceptor {
    private const val TOKEN = "c95d0e92-1c58-40b6-9df5-6ed542cb8233"

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val builder = request.newBuilder().addHeader("Authorization", TOKEN)
        val newRequest = builder.build()

        return chain.proceed(newRequest)
    }
}