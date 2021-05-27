package com.example.learnkotlin.modules

import android.content.Context
import com.example.data.datebases.HabitTrackerDatabase
import com.example.data.interfaces.HabitDao
import com.example.data.interfaces.HabitService
import com.example.data.network.RetrofitNetwork
import com.example.data.repositories.HabitRepositoryImpl
import com.example.domain.interfaces.HabitRepository
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
class HabitsModule(private val context: Context) {
    @Provides
    fun provideCoroutineDispatcher(): CoroutineDispatcher {
        return Dispatchers.IO
    }

    @Provides
    fun provideHabitRepository(): HabitRepository {
        return HabitRepositoryImpl(provideHabitDao(), provideHabitService())
    }

    @Provides
    fun provideHabitDao(): HabitDao {
        return HabitTrackerDatabase.getInstance(provideContext()).habitDao()
    }

    @Provides
    fun provideContext(): Context {
        return context
    }

    @Provides
    fun provideHabitService(): HabitService {
        return provideRetrofit().create(HabitService::class.java)
    }

    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit {
        return RetrofitNetwork().retrofit
    }
}