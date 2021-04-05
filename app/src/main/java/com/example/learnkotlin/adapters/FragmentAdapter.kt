package com.example.learnkotlin.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.learnkotlin.HabitListFragment
import com.example.learnkotlin.models.HabitElement

class FragmentAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle,
    private val positiveHabits: ArrayList<HabitElement>,
    private val negativeHabits: ArrayList<HabitElement>
) : FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            1 -> HabitListFragment.newInstance(negativeHabits)
            else -> HabitListFragment.newInstance(positiveHabits)
        }
    }
}