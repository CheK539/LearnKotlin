package com.example.learnkotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.learnkotlin.adapters.HabitAdapter
import com.example.learnkotlin.databinding.ActivityMainBinding
import com.example.learnkotlin.interfaces.IDisplayFormCallback
import com.example.learnkotlin.models.HabitElement


private const val EXTRA_HABIT_ELEMENTS = "HabitElements"

class MainActivity : AppCompatActivity(), IDisplayFormCallback {
    private lateinit var binding: ActivityMainBinding
    private var habitElements = arrayListOf<HabitElement>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        if (savedInstanceState == null) {
            val fragment = HabitListFragment.newInstance(habitElements)
            supportFragmentManager.beginTransaction().add(R.id.mainContextFragment, fragment)
                .commit()
        }
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

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}