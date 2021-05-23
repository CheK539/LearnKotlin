package com.example.learnkotlin.components

import com.example.learnkotlin.fragments.HabitsListFragment
import com.example.learnkotlin.fragments.MainFragment
import com.example.learnkotlin.fragments.SearchFragment
import dagger.Subcomponent

@Subcomponent
interface HabitsComponent {
    @Subcomponent.Factory
    interface Factory {
        fun create(): HabitsComponent
    }

    fun inject(mainFragment: MainFragment)

    fun inject(searchFragment: SearchFragment)

    fun inject(habitsListFragment: HabitsListFragment)
}