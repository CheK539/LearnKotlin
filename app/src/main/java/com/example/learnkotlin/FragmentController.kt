package com.example.learnkotlin

import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import com.example.learnkotlin.models.HabitElement

object FragmentController {
    fun openDisplayFormFragment(navController: NavController, habitElement: HabitElement?) {
        navController.navigate(R.id.action_homePage_to_displayFormPage, Bundle().apply {
            putParcelable(ARGS_HABIT_ELEMENT, habitElement)
        })
    }

    fun openMainFragment(navController: NavController) {
        val navOption = NavOptions.Builder().setPopUpTo(R.id.main_controller, true).build()
        navController.navigate(R.id.homePage, null, navOption)
    }

    fun backToMainFragment(navController: NavController) {
        navController.navigateUp()
    }

    fun openAboutFragment(navController: NavController) {
        navController.navigate(R.id.aboutPage)
    }
}