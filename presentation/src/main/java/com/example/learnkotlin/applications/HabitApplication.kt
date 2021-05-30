package com.example.learnkotlin.applications

import android.app.Application
import com.example.learnkotlin.di.components.DaggerApplicationComponent
import com.example.learnkotlin.di.components.ApplicationComponent
import com.example.learnkotlin.di.modules.HabitsModule

class HabitApplication : Application() {
    lateinit var applicationComponent: ApplicationComponent
        private set

    override fun onCreate() {
        super.onCreate()

        applicationComponent = DaggerApplicationComponent
            .builder()
            .habitsModule(HabitsModule(this))
            .build()
    }
}