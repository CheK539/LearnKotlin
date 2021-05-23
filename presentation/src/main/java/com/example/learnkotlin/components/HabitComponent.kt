package com.example.learnkotlin.components

import com.example.learnkotlin.factories.HabitsModule
import com.example.learnkotlin.fragments.DisplayFormFragment
import com.example.learnkotlin.fragments.HabitsListFragment
import com.example.learnkotlin.fragments.MainFragment
import com.example.learnkotlin.fragments.SearchFragment
import dagger.Component

@Component(modules = [HabitsModule::class])
interface HabitComponent {
    fun inject(mainFragment: MainFragment)

    fun inject(searchFragment: SearchFragment)

    fun inject(habitsListFragment: HabitsListFragment)

    fun inject(displayFormFragment: DisplayFormFragment)
}