package com.example.learnkotlin.interceptors

import com.example.learnkotlin.TOKEN
import okhttp3.Interceptor
import okhttp3.Response

object AuthorizationInterceptor : Interceptor{
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val builder = request.newBuilder().addHeader("Authorization", TOKEN)
        val newRequest = builder.build()

        return chain.proceed(newRequest)
    }
}