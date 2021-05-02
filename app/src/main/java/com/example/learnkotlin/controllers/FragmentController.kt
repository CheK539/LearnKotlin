package com.example.learnkotlin.controllers

import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import com.example.learnkotlin.ARGS_HABIT_ELEMENT
import com.example.learnkotlin.R

object FragmentController {
    fun openDisplayFormFragment(navController: NavController, id: Int?) {
        navController.navigate(R.id.action_homePage_to_displayFormPage, Bundle().apply {
            putInt(ARGS_HABIT_ELEMENT, id ?: -1)
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