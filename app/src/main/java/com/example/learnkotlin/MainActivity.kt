package com.example.learnkotlin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.learnkotlin.databinding.ActivityMainBinding
import com.example.learnkotlin.enums.ResultAction
import com.example.learnkotlin.adapters.HabitAdapter
import com.example.learnkotlin.models.HabitElement

const val EXTRA_POSITION = "habitPosition"
const val EXTRA_DELETE_POSITION = "deletePosition"
const val EXTRA_HABIT_ELEMENT = "habitElement"
const val EXTRA_ADDED_HABIT = "addedHabit"
const val EXTRA_REPLACE_HABIT = "replaceHabit"

class MainActivity : AppCompatActivity(), HabitAdapter.OnHabitListener {
    private lateinit var binding: ActivityMainBinding

    private val habitElements = mutableListOf<HabitElement>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        binding.recycleView.adapter = HabitAdapter(habitElements, this)
        binding.recycleView.layoutManager = LinearLayoutManager(this)
        binding.addButton.setOnClickListener {
            onHabitClick(-1)
            binding.addButton.hide()
        }
    }

    override fun onRestart() {
        binding.addButton.show()
        binding.recycleView.adapter?.notifyDataSetChanged()
        super.onRestart()
    }

    override fun onHabitClick(position: Int) {
        val habitElement =
            if (position < 0 || position >= habitElements.size) null else habitElements[position]
        IntentSender.sendToDisplayFormActivity(this, position, habitElement)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (data == null)
            return super.onActivityResult(requestCode, resultCode, data)

        when (resultCode) {
            ResultAction.Add.number -> addHabit(data)

            ResultAction.Replace.number -> replaceHabit(data)

            ResultAction.Delete.number -> deleteHabit(data)
        }
    }

    private fun addHabit(data: Intent) {
        val addedHabit = data.getParcelableExtra<HabitElement>(EXTRA_ADDED_HABIT)
        if (addedHabit != null) {
            habitElements.add(addedHabit)
            binding.recycleView.adapter?.notifyDataSetChanged()
        } else
            throw IllegalArgumentException("Haven't habit on added action")
    }

    private fun replaceHabit(data: Intent) {
        val replaceHabit = data.getParcelableExtra<HabitElement>(EXTRA_REPLACE_HABIT)
        val position = data.getIntExtra(EXTRA_POSITION, -1)
        if (replaceHabit != null && position >= 0) {
            habitElements[position] = replaceHabit
            binding.recycleView.adapter?.notifyDataSetChanged()
        } else
            throw IllegalArgumentException("Haven't habit on replace action")
    }

    private fun deleteHabit(data: Intent) {
        val deletePosition = data.getIntExtra(EXTRA_DELETE_POSITION, -1)
        if (deletePosition >= 0) {
            habitElements.removeAt(deletePosition)
            binding.recycleView.adapter?.notifyItemRemoved(deletePosition)
        } else
            throw IllegalArgumentException("Haven't position on delete action")
    }
}