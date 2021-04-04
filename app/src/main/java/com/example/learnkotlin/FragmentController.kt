package com.example.learnkotlin

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import com.example.learnkotlin.models.HabitElement

object FragmentController {
    fun openDisplayFormFragment(
        activity: FragmentActivity?,
        habitElement: HabitElement?,
        position: Int
    ) {
        val fragment = if (habitElement != null)
            DisplayFormFragment.newInstance(habitElement, position)
        else
            DisplayFormFragment.newInstance()

        activity?.supportFragmentManager?.beginTransaction()
            ?.replace(R.id.mainContextFragment, fragment)?.addToBackStack(null)?.commit()
    }

    fun openHabitListFragment(activity: AppCompatActivity, habitElements: ArrayList<HabitElement>) {
        val fragment = HabitListFragment.newInstance(habitElements)
        activity.supportFragmentManager.beginTransaction()
            .replace(R.id.mainContextFragment, fragment).commit()
    }
}