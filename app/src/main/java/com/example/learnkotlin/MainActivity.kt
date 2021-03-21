package com.example.learnkotlin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.learnkotlin.databinding.ActivityMainBinding
import com.example.learnkotlin.habitModel.HabitAdapter
import com.example.learnkotlin.habitModel.HabitElement

const val EXTRA_MESSAGE = "com.example.learnkotlin.MESSAGE"
val habitElements = mutableListOf<HabitElement>()

class MainActivity : AppCompatActivity(), HabitAdapter.OnHabitListener {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        binding.recycleView.adapter = HabitAdapter(habitElements, this)
        binding.recycleView.layoutManager = LinearLayoutManager(this)
        binding.addButton.setOnClickListener { view ->
            createElement(view)
            binding.addButton.hide()
        }
    }

    override fun onRestart() {
        binding.addButton.show()
        binding.recycleView.adapter?.notifyDataSetChanged()
        super.onRestart()
    }

    private fun createElement(view: View) {
        val intent = Intent(this, DisplayFormActivity::class.java)
        startActivity(intent)
    }

    override fun onHabitClick(position: Int) {
        val intent = Intent(this, DisplayFormActivity::class.java)
        intent.putExtra(EXTRA_MESSAGE, position)
        startActivity(intent)
    }
}