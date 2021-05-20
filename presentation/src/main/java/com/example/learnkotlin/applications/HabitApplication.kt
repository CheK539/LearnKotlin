package com.example.learnkotlin.applications

import android.app.Application
import com.example.learnkotlin.factories.HabitFactory

class HabitApplication : Application() {
    lateinit var habitFactory: HabitFactory
        private set

    override fun onCreate() {
        super.onCreate()

        habitFactory = HabitFactory(this)
    }
}