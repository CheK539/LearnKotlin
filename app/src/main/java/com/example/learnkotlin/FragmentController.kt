package com.example.learnkotlin

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import com.example.learnkotlin.models.HabitElement

object FragmentController {
    fun openDisplayFormFragment(
        activity: AppCompatActivity, habitElement: HabitElement?, position: Int
    ) {
        val navigation = Navigation.findNavController(activity, R.id.mainControllerContext)

        navigation.navigate(R.id.action_homePage_to_displayFormPage, Bundle().apply {
            putParcelable(ARGS_HABIT_ELEMENT, habitElement)
            putInt(ARGS_POSITION, position)
        })
    }

    fun openMainFragment(activity: AppCompatActivity, habitElements: ArrayList<HabitElement>) {
        val navOption = NavOptions.Builder().setPopUpTo(R.id.main_controller, true).build()
        val navigation = Navigation.findNavController(activity, R.id.mainControllerContext)
        val bundle = Bundle().apply {
            putParcelableArrayList(ARGS_HABIT_ELEMENTS, habitElements)
        }
        navigation.navigate(R.id.homePage, bundle, navOption)
    }

    fun openAboutFragment(activity: AppCompatActivity) {
        val navigation = Navigation.findNavController(activity, R.id.mainControllerContext)
        navigation.navigate(R.id.action_homePage_to_aboutPage)
    }
}