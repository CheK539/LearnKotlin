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
import com.example.learnkotlin.databinding.ActivityDisplayFormBinding
import com.example.learnkotlin.habitModel.HabitElement

class DisplayFormActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    private lateinit var binding: ActivityDisplayFormBinding
    private var priorityChoice: String = ""
    private var isNewHabit = true
    private var position = -1
    private lateinit var arrayAdapter: ArrayAdapter<CharSequence>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_display_form)

        arrayAdapter = ArrayAdapter.createFromResource(
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
            fillEditForm(position)
        }
    }

    private fun fillEditForm(position: Int) {
        val habit = habitElements[position]
        binding.titleEditText.setText(habit.title)
        binding.descriptionEditText.setText(habit.description)

        arrayAdapter.apply { binding.prioritySpinner.setSelection(getPosition(habit.priority)) }

        binding.completeCounter.setText(habit.completeCounter)
        binding.periodNumber.setText(habit.periodNumber)
        binding.typesGroup.check(
            if (habit.type == binding.negativeType.text.toString())
                R.id.negativeType
            else R.id.positiveType
        )
    }

    private fun validateForm(): Boolean {
        if (binding.titleEditText.text.toString().isEmpty()) {
            binding.textInputTitle.error = "Title cannot be empty."
            return false
        }

        return true
    }

    private fun createHabit(): HabitElement =
        HabitElement(
            binding.titleEditText.text.toString(),
            binding.descriptionEditText.text.toString(),
            priorityChoice,
            findViewById<RadioButton>(binding.typesGroup.checkedRadioButtonId).text.toString(),
            binding.completeCounter.text.toString(),
            binding.periodNumber.text.toString(),
            "None"
        )

    private fun changeHabit() {
        if (!validateForm())
            return

        habitElements[position] = createHabit()
        finish()
    }

    private fun addHabit() {
        if (!validateForm())
            return

        habitElements.add(createHabit())
        finish()
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
                addHabit()
            else
                changeHabit()
            true
        }

        else -> {
            super.onOptionsItemSelected(item)
        }
    }
}