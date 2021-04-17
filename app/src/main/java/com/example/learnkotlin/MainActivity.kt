package com.example.learnkotlin

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.example.learnkotlin.databinding.ActivityMainBinding
import com.example.learnkotlin.interfaces.IDisplayFormCallback
import com.example.learnkotlin.models.HabitElement
import com.example.learnkotlin.viewModels.HabitsViewModel
import com.google.android.material.navigation.NavigationView


const val ARGS_HABIT_ELEMENTS = "habitElements"
const val ARGS_HABIT_ELEMENT = "habitElement"
const val ARGS_POSITION = "position"

class MainActivity : AppCompatActivity(), IDisplayFormCallback,
    NavigationView.OnNavigationItemSelectedListener {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navigation: NavController
    private lateinit var habitsViewModel: HabitsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        if (savedInstanceState == null)
            binding.navigationView.setCheckedItem(R.id.home_page)

        changeActivity()
    }

    private fun changeActivity() {
        setSupportActionBar(binding.toolbar)
        navigation = Navigation.findNavController(this, R.id.mainControllerContext)
        NavigationUI.setupActionBarWithNavController(this, navigation, binding.drawerLayout)
        NavigationUI.setupWithNavController(binding.navigationView, navigation)
        binding.navigationView.setNavigationItemSelectedListener(this)

        habitsViewModel = ViewModelProvider(this).get(HabitsViewModel::class.java)
    }

    override fun addHabit(habitElement: HabitElement) {
        habitsViewModel.addHabit(habitElement)
        FragmentController.backToMainFragment(this)
    }

    override fun replaceHabit(oldHabitElement: HabitElement, newHabitElement: HabitElement) {
        replaceHabitFields(oldHabitElement, newHabitElement)
        FragmentController.openMainFragment(this)
    }

    override fun deleteHabit(habitElement: HabitElement) {
        habitsViewModel.deleteHabit(habitElement)
        FragmentController.openMainFragment(this)
    }

    private fun replaceHabitFields(oldHabitElement: HabitElement, newHabitElement: HabitElement) {
        oldHabitElement.title = newHabitElement.title
        oldHabitElement.description = newHabitElement.description
        oldHabitElement.type = newHabitElement.type
        oldHabitElement.priority = newHabitElement.priority
        oldHabitElement.color = newHabitElement.color
        oldHabitElement.completeCounter = newHabitElement.completeCounter
        oldHabitElement.periodNumber = newHabitElement.periodNumber
    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(navigation, binding.drawerLayout)
    }

    override fun onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START))
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        else
            super.onBackPressed()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.home_page -> FragmentController.openMainFragment(this)

            R.id.about_page -> FragmentController.openAboutFragment(this)
        }

        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
}