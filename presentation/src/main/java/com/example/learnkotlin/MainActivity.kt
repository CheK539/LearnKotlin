package com.example.learnkotlin

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.bumptech.glide.Glide
import com.example.learnkotlin.databinding.ActivityMainBinding


const val ARGS_HABIT_ELEMENT = "habitElement"

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        changeActivity()
    }

    private fun changeActivity() {
        setSupportActionBar(binding.toolbar)

        val headerLayout = binding.navigationView.getHeaderView(0)
        val avatarImage = headerLayout.findViewById<ImageView>(R.id.avatar)
        Glide.with(this)
            .load("https://avatarfiles.alphacoders.com/274/274303.png")
            .placeholder(R.drawable.ahe_launcher_round)
            .error(R.drawable.error_image_round)
            .circleCrop()
            .into(avatarImage)

        appBarConfiguration = AppBarConfiguration(
            setOf(R.id.homePage, R.id.aboutPage), binding.drawerLayout
        )

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.mainControllerContext)
                as NavHostFragment
        navController = navHostFragment.navController
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration)
        NavigationUI.setupWithNavController(binding.navigationView, navController)
    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(navController, appBarConfiguration)
    }

    override fun onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START))
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        else
            super.onBackPressed()
    }
}