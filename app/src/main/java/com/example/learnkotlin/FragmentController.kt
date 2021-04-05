package com.example.learnkotlin

import androidx.appcompat.app.AppCompatActivity
import com.example.learnkotlin.models.HabitElement

object FragmentController {
    fun openDisplayFormFragment(
        activity: AppCompatActivity, habitElement: HabitElement?, position: Int
    ) {
        val fragment = if (habitElement != null)
            DisplayFormFragment.newInstance(habitElement, position)
        else
            DisplayFormFragment.newInstance()

        activity.supportFragmentManager.beginTransaction()
            .replace(R.id.mainContextFragment, fragment).addToBackStack("toHabitList").commit()
    }

    fun openHabitListFragment(activity: AppCompatActivity, habitElements: ArrayList<HabitElement>) {
        val fragment = MainFragment.newInstance(habitElements)
        activity.supportFragmentManager.beginTransaction()
            .replace(R.id.mainContextFragment, fragment).commit()
    }

    fun openAboutFragment(activity: AppCompatActivity) {
        val fragment = AboutFragment.newInstance()
        activity.supportFragmentManager.beginTransaction()
            .replace(R.id.mainContextFragment, fragment).commit()
    }
}