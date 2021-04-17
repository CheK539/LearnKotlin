package com.example.learnkotlin.repositories

import androidx.lifecycle.MutableLiveData
import com.example.learnkotlin.models.HabitElement

class HabitElementRepository {
    companion object {
        val instance: HabitElementRepository = HabitElementRepository()
    }

    val habitElements = MutableLiveData<ArrayList<HabitElement>>().apply { value = ArrayList() }
}