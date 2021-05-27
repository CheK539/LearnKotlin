package com.example.learnkotlin.components

import com.example.learnkotlin.modules.HabitsModule
import com.example.learnkotlin.modules.SubcomponentsModule
import com.example.learnkotlin.fragments.DisplayFormFragment
import com.example.learnkotlin.fragments.HabitsListFragment
import com.example.learnkotlin.fragments.MainFragment
import com.example.learnkotlin.fragments.SearchFragment
import dagger.Component
import javax.inject.Singleton

@FragmentScope
@Singleton
@Component(modules = [HabitsModule::class, SubcomponentsModule::class])
interface ApplicationComponent {
    fun habitsComponent(): HabitsComponent.Factory

    fun inject(mainFragment: MainFragment)

    fun inject(searchFragment: SearchFragment)

    fun inject(habitsListFragment: HabitsListFragment)

    fun inject(displayFormFragment: DisplayFormFragment)
}