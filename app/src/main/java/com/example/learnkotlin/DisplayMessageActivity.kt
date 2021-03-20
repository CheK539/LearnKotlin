package com.example.learnkotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.RadioButton
import androidx.databinding.DataBindingUtil
import com.example.learnkotlin.databinding.ActivityDisplayMessageBinding
import com.example.learnkotlin.habitModel.HabitElement

class DisplayMessageActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    private lateinit var binding: ActivityDisplayMessageBinding
    private var priorityChoice: String = ""
    private var isNewHabit = true
    private var position = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_display_message)

        ArrayAdapter.createFromResource(
            this,
            R.array.priorities,
            R.layout.priority_item
        ).also { adapter ->
            binding.prioritySpinner.adapter = adapter
            binding.prioritySpinner.onItemSelectedListener = this
        }

        position = intent.getIntExtra(EXTRA_MESSAGE, -1)
        if (position >= 0) {
            isNewHabit = false
            editUser(position)
        }
    }

    private fun editUser(position: Int) {
        val habit = habitElements[position]
        binding.titleEditText.setText(habit.title)
        binding.descriptionEditText.setText(habit.description)

        ArrayAdapter.createFromResource(
            this,
            R.array.priorities,
            android.R.layout.simple_spinner_item
        ).apply { binding.prioritySpinner.setSelection(getPosition(habit.priority)) }

        binding.completeCounter.setText(habit.completeCounter)
        binding.periodNumber.setText(habit.periodNumber)
        binding.typesGroup.check(
            if (habit.type == binding.negativeType.text.toString())
                R.id.negativeType
            else R.id.positiveType
        )
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        priorityChoice = parent?.getItemAtPosition(position).toString()
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_tool_buttons, menu)
        return super.onCreateOptionsMenu(menu)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.action_save -> {
            if (isNewHabit)
                addUser()
            else
                changeUser()
            true
        }

        else -> {
            super.onOptionsItemSelected(item)
        }
    }

    private fun changeUser() {
        if (binding.titleEditText.text.toString() == "")
            return

        habitElements[position].apply {
            title = binding.titleEditText.text.toString()
            description = binding.descriptionEditText.text.toString()
            priority = priorityChoice
            type =
                findViewById<RadioButton>(binding.typesGroup.checkedRadioButtonId).text.toString()
            completeCounter = binding.completeCounter.text.toString()
            periodNumber = binding.periodNumber.text.toString()
        }
        finish()
    }

    private fun addUser() {
        if (binding.titleEditText.text.toString() == "")
            return

        habitElements.add(
            HabitElement(
                binding.titleEditText.text.toString(),
                binding.descriptionEditText.text.toString(),
                priorityChoice,
                findViewById<RadioButton>(binding.typesGroup.checkedRadioButtonId).text.toString(),
                binding.completeCounter.text.toString(),
                binding.periodNumber.text.toString(),
                "None"
            )
        )
        finish()
    }
}