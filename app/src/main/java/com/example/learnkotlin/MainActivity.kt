package com.example.learnkotlin

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import com.example.learnkotlin.databinding.ActivityMainBinding
import com.example.learnkotlin.interfaces.IDisplayFormCallback
import com.example.learnkotlin.models.HabitElement
import com.google.android.material.navigation.NavigationView


private const val EXTRA_HABIT_ELEMENTS = "HabitElements"

class MainActivity : AppCompatActivity(), IDisplayFormCallback,
    NavigationView.OnNavigationItemSelectedListener {
    private lateinit var binding: ActivityMainBinding
    private var habitElements = arrayListOf<HabitElement>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        if (savedInstanceState == null) {
            FragmentController.openHabitListFragment(this, habitElements)
            binding.navigationView.setCheckedItem(R.id.home_page)
        }

        setSupportActionBar(binding.toolbar)
        binding.navigationView.setNavigationItemSelectedListener(this)
    }

    override fun addHabit(habitElement: HabitElement) {
        habitElements.add(habitElement)
        supportFragmentManager.popBackStack()
        FragmentController.openHabitListFragment(this, habitElements)
    }

    override fun replaceHabit(oldHabitElement: HabitElement, newHabitElement: HabitElement) {
        replaceHabitFields(oldHabitElement, newHabitElement)
        supportFragmentManager.popBackStack()
        FragmentController.openHabitListFragment(this, habitElements)
    }

    override fun deleteHabit(habitElement: HabitElement) {
        habitElements.remove(habitElement)
        supportFragmentManager.popBackStack()
        FragmentController.openHabitListFragment(this, habitElements)
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

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(EXTRA_HABIT_ELEMENTS, habitElements)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        habitElements =
            savedInstanceState.getParcelableArrayList(EXTRA_HABIT_ELEMENTS) ?: habitElements
    }

    override fun onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START))
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        else
            super.onBackPressed()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.home_page -> FragmentController.openHabitListFragment(this, habitElements)

            R.id.about_page -> FragmentController.openAboutFragment(this)
        }

        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
}