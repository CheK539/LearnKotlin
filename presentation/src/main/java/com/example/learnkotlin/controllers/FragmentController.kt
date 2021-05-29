package com.example.learnkotlin.controllers

import android.os.Bundle
import androidx.navigation.NavController
import com.example.learnkotlin.ARGS_HABIT_ELEMENT
import com.example.learnkotlin.R

object FragmentController {
    fun openDisplayFormFragment(navController: NavController, uid: String?) {
        navController.navigate(R.id.action_homePage_to_displayFormPage, Bundle().apply {
            putString(ARGS_HABIT_ELEMENT, uid)
        })
    }

    fun backToMainFragment(navController: NavController) {
        navController.navigateUp()
    }

}