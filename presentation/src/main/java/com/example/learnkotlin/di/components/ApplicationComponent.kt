package com.example.learnkotlin.di.components

import com.example.learnkotlin.di.modules.HabitsModule
import com.example.learnkotlin.di.modules.SubcomponentsModule
import com.example.learnkotlin.fragments.DisplayFormFragment
import com.example.learnkotlin.fragments.HabitsListFragment
import com.example.learnkotlin.fragments.MainFragment
import com.example.learnkotlin.fragments.SearchFragment
import com.example.learnkotlin.di.scopes.ViewModelScope
import dagger.Component
import javax.inject.Singleton

@ViewModelScope
@Singleton
@Component(modules = [HabitsModule::class, SubcomponentsModule::class])
interface ApplicationComponent {
    fun habitsComponent(): HabitsComponent.Factory

    fun inject(mainFragment: MainFragment)

    fun inject(searchFragment: SearchFragment)

    fun inject(habitsListFragment: HabitsListFragment)

    fun inject(displayFormFragment: DisplayFormFragment)
}