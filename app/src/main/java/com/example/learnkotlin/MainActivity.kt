package com.example.learnkotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import com.example.learnkotlin.adapters.HabitAdapter
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

        setSupportActionBar(binding.toolbar)
        if (savedInstanceState == null) {
            FragmentController.openHabitListFragment(this, habitElements)
            binding.navigationView.setCheckedItem(R.id.home_page)
        }

        //createToggle()

        binding.navigationView.setNavigationItemSelectedListener(this)
    }

    private fun createToggle() {
        val toggle = ActionBarDrawerToggle(
            this,
            binding.drawerLayout,
            binding.toolbar,
            R.string.drawer_open,
            R.string.drawer_close
        )

        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
    }

    override fun addHabit(habitElement: HabitElement) {
        habitElements.add(habitElement)
        supportFragmentManager.popBackStack()
        FragmentController.openHabitListFragment(this, habitElements)
    }

    override fun replaceHabit(habitElement: HabitElement, position: Int) {
        if (position >= 0) {
            habitElements[position] = habitElement
            supportFragmentManager.popBackStack()
            FragmentController.openHabitListFragment(this, habitElements)
        } else
            throw IllegalArgumentException("Haven't habit on replace action")
    }

    override fun deleteHabit(position: Int) {
        if (position >= 0) {
            habitElements.removeAt(position)
            supportFragmentManager.popBackStack()
            FragmentController.openHabitListFragment(this, habitElements)
        } else
            throw IllegalArgumentException("Haven't position on delete action")
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