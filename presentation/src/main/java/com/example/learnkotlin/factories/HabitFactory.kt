package com.example.learnkotlin.factories

import android.content.Context
import com.example.data.datebases.HabitTrackerDatabase
import com.example.data.interfaces.HabitDao
import com.example.data.interfaces.HabitService
import com.example.data.network.RetrofitNetwork
import com.example.data.repositories.HabitRepositoryImpl
import com.example.domain.usecases.HabitsUseCase
import com.example.domain.usecases.FormUseCase
import kotlinx.coroutines.Dispatchers
import retrofit2.Retrofit

class HabitFactory(private val context: Context) {
    fun provideHabitsUseCase(): HabitsUseCase {
        return HabitsUseCase(provideHabitRepository(), Dispatchers.IO)
    }

    fun provideFormUseCase(): FormUseCase {
        return FormUseCase(provideHabitRepository(), Dispatchers.IO)
    }

    fun provideHabitRepository(): HabitRepositoryImpl {
        return HabitRepositoryImpl.getInstance(provideHabitDao(), provideHabitService())
    }

    fun provideHabitDao(): HabitDao {
        return HabitTrackerDatabase.getInstance(provideContext()).habitDao()
    }

    fun provideContext(): Context {
        return context
    }

    fun provideHabitService(): HabitService {
        return provideRetrofit().create(HabitService::class.java)
    }

    fun provideRetrofit(): Retrofit {
        return RetrofitNetwork.getInstance().retrofit
    }
}