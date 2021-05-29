package com.example.learnkotlin.modules

import android.content.Context
import com.example.data.builders.HabitTrackerDataBaseBuilder
import com.example.data.builders.RetrofitBuilder
import com.example.data.interfaces.HabitService
import com.example.data.repositories.HabitRepositoryImpl
import com.example.domain.interfaces.HabitRepository
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module
class HabitsModule(private val context: Context) {
    @Provides
    fun provideCoroutineDispatcher() = Dispatchers.IO

    @Provides
    fun provideContext() = context

    @Provides
    fun provideHabitDao() = provideHabitTrackerDatabase().habitDao()

    @Provides
    fun provideHabitService(): HabitService = provideRetrofit().create(HabitService::class.java)

    @Singleton
    @Provides
    fun provideHabitTrackerDatabase() = HabitTrackerDataBaseBuilder(provideContext()).build()

    @Singleton
    @Provides
    fun provideHabitRepository(): HabitRepository =
        HabitRepositoryImpl(provideHabitDao(), provideHabitService())

    @Singleton
    @Provides
    fun provideRetrofit() = RetrofitBuilder().build()
}