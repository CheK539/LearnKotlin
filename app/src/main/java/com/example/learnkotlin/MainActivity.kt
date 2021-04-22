package com.example.learnkotlin

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.example.learnkotlin.databinding.ActivityMainBinding
import com.example.learnkotlin.interfaces.IDisplayFormCallback
import com.example.learnkotlin.models.HabitElement
import com.example.learnkotlin.viewModels.HabitsViewModel


const val ARGS_HABIT_ELEMENTS = "habitElements"
const val ARGS_HABIT_ELEMENT = "habitElement"

class MainActivity : AppCompatActivity(), IDisplayFormCallback {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navigation: NavController
    private lateinit var habitsViewModel: HabitsViewModel

    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        changeActivity()
    }

    private fun changeActivity() {
        setSupportActionBar(binding.toolbar)

        appBarConfiguration = AppBarConfiguration(
            setOf(R.id.homePage, R.id.aboutPage), binding.drawerLayout
        )
        navigation = Navigation.findNavController(this, R.id.mainControllerContext)
        NavigationUI.setupActionBarWithNavController(this, navigation, appBarConfiguration)
        NavigationUI.setupWithNavController(binding.navigationView, navigation)

        habitsViewModel = ViewModelProvider(this).get(HabitsViewModel::class.java)
    }

    override fun addHabit(habitElement: HabitElement) {
        habitsViewModel.addHabit(habitElement)
        FragmentController.backToMainFragment(this)
    }

    override fun deleteHabit(habitElement: HabitElement) {
        habitsViewModel.deleteHabit(habitElement)
        FragmentController.backToMainFragment(this)
    }

    override fun habitChanged() {
        FragmentController.openMainFragment(this)
    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(navigation, appBarConfiguration)
    }

    override fun onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START))
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        else
            super.onBackPressed()
    }
}