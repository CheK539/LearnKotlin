package com.example.learnkotlin.applications

import android.app.Application
import com.example.learnkotlin.components.DaggerHabitComponent
import com.example.learnkotlin.components.HabitComponent
import com.example.learnkotlin.factories.HabitsModule

class HabitApplication : Application() {
    lateinit var habitsModule: HabitComponent
        private set

    override fun onCreate() {
        super.onCreate()

        habitsModule = DaggerHabitComponent
            .builder()
            .habitsModule(HabitsModule(this))
            .build()
    }
}