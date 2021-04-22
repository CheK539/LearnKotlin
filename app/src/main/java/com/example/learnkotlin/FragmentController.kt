package com.example.learnkotlin

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import com.example.learnkotlin.models.HabitElement

object FragmentController {
    fun openDisplayFormFragment(activity: AppCompatActivity, habitElement: HabitElement?) {
        val navigation = Navigation.findNavController(activity, R.id.mainControllerContext)
        navigation.navigate(R.id.action_homePage_to_displayFormPage, Bundle().apply {
            putParcelable(ARGS_HABIT_ELEMENT, habitElement)
        })
    }

    fun openMainFragment(activity: AppCompatActivity) {
        val navOption = NavOptions.Builder().setPopUpTo(R.id.main_controller, true).build()
        val navigation = Navigation.findNavController(activity, R.id.mainControllerContext)
        navigation.navigate(R.id.homePage, null, navOption)
    }

    fun backToMainFragment(activity: AppCompatActivity) {
        val navigation = Navigation.findNavController(activity, R.id.mainControllerContext)
        navigation.popBackStack()
    }

    fun openAboutFragment(activity: AppCompatActivity) {
        val navigation = Navigation.findNavController(activity, R.id.mainControllerContext)
        navigation.navigate(R.id.aboutPage)
    }
}