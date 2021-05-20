package com.example.learnkotlin.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.learnkotlin.fragments.HabitsListFragment
import com.example.domain.enums.HabitType

class FragmentAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle,
    private val firstHabitType: HabitType,
    private val secondHabitType: HabitType
) : FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            1 -> HabitsListFragment.newInstance(secondHabitType)
            else -> HabitsListFragment.newInstance(firstHabitType)
        }
    }
}