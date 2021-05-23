package com.example.learnkotlin.applications

import android.app.Application
import com.example.learnkotlin.components.DaggerApplicationComponent
import com.example.learnkotlin.components.ApplicationComponent
import com.example.learnkotlin.modules.HabitsModule

class HabitApplication : Application() {
    lateinit var habitsModule: ApplicationComponent
        private set

    override fun onCreate() {
        super.onCreate()

        habitsModule = DaggerApplicationComponent
            .builder()
            .habitsModule(HabitsModule(this))
            .build()
    }
}