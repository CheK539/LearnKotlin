package com.example.learnkotlin

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.learnkotlin.databinding.ActivityMainBinding
import com.example.learnkotlin.habitModel.HabitAdapter
import com.example.learnkotlin.habitModel.HabitElement

const val EXTRA_MESSAGE = "com.example.learnkotlin.MESSAGE"

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val habitElements: List<HabitElement> = listOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        binding.recycleView.adapter = HabitAdapter(habitElements)
        binding.recycleView.layoutManager = LinearLayoutManager(this)
    }

    /*fun sendMessage(view: View) {
        val editText = binding.counter
        val message = editText.text.toString()
        val intent = Intent(this, DisplayMessageActivity::class.java).apply {
            putExtra(EXTRA_MESSAGE, message)
        }
        startActivity(intent)
    }*/
}