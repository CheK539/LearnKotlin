package com.example.learnkotlin

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.drawToBitmap
import androidx.databinding.DataBindingUtil
import com.example.learnkotlin.databinding.ActivityDisplayFormBinding
import com.example.learnkotlin.enums.ResultAction
import com.example.learnkotlin.models.HabitElement


class DisplayFormActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    private lateinit var binding: ActivityDisplayFormBinding
    private lateinit var arrayAdapter: ArrayAdapter<CharSequence>
    private var priorityChoice: String = ""
    private var isNewHabit = true
    private var position = -1

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

        position = intent.getIntExtra(EXTRA_POSITION, -1)

        if (position >= 0) {
            isNewHabit = false
            fillEditForm()
        }

        addGlobalColorLayoutListener()
    }

    private fun addGlobalColorLayoutListener() =
        binding.colorsLayout.viewTreeObserver.addOnGlobalLayoutListener(
            object : OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    binding.colorsLayout.viewTreeObserver.removeOnGlobalLayoutListener(this)
                    fillButtonColors()
                }
            }
        )

    private fun fillButtonColors() {
        val childCount = binding.colorsLayout.childCount
        val bitmap = binding.colorsLayout.drawToBitmap()

        for (i in 0 until childCount) {
            val view = binding.colorsLayout.getChildAt(i)
            val (x, y) = getCenterPosition(view, binding.colorsLayout)
            val pixel = bitmap.getPixel(x, y)
            view.setBackgroundColor(pixel)
        }
    }

    private fun getCenterPosition(view: View, layout: LinearLayout): Pair<Int, Int> {
        val rectanglePosition = IntArray(2)
        val layoutPosition = IntArray(2)
        view.getLocationOnScreen(rectanglePosition)
        layout.getLocationOnScreen(layoutPosition)
        val x = rectanglePosition[0] - layoutPosition[0] + view.width / 2
        val y = rectanglePosition[1] - layoutPosition[1] + view.height / 2
        return Pair(x, y)
    }

    private fun fillEditForm() {
        val habit = intent.getParcelableExtra<HabitElement>(EXTRA_HABIT_ELEMENT) ?: return
        binding.titleEditText.setText(habit.title)
        binding.descriptionEditText.setText(habit.description)
        binding.completeCounter.setText(habit.completeCounter.toString())
        binding.periodNumber.setText(habit.periodNumber)
        binding.colorButton.setBackgroundColor(Color.parseColor(habit.color.split(" ")[0]))
        binding.typesGroup.check(
            if (habit.type == binding.negativeType.text.toString())
                R.id.negativeType
            else R.id.positiveType
        )

        arrayAdapter.apply { binding.prioritySpinner.setSelection(getPosition(habit.priority)) }
    }

    private fun validateForm(): Boolean {
        if (binding.titleEditText.text.toString().isEmpty()) {
            binding.textInputTitle.error = "Title cannot be empty."
            return false
        }

        return true
    }

    private fun createHabit(): HabitElement {
        val rgb = (binding.colorButton.background as ColorDrawable).color
        val hsv = FloatArray(3)
        Color.colorToHSV(rgb, hsv)
        val hexColor = "#${"%x".format(rgb)}"

        return HabitElement(
            binding.titleEditText.text.toString(),
            binding.descriptionEditText.text.toString(),
            priorityChoice,
            findViewById<RadioButton>(binding.typesGroup.checkedRadioButtonId).text.toString(),
            binding.completeCounter.text.toString().toIntOrNull() ?: 0,
            binding.periodNumber.text.toString(),
            "$hexColor ${hsv[0]} ${hsv[1]} ${hsv[2]}"
        )
    }

    private fun changeHabit() {
        if (!validateForm())
            return

        val intent = Intent().apply {
            putExtra(EXTRA_REPLACE_HABIT, createHabit())
            putExtra(EXTRA_POSITION, position)
        }

        setResult(ResultAction.Replace.number, intent)
        finish()
    }

    private fun addHabit() {
        if (!validateForm())
            return

        setResult(
            ResultAction.Add.number,
            Intent().apply { putExtra(EXTRA_ADDED_HABIT, createHabit()) })

        finish()
    }

    private fun deleteHabit() {
        setResult(
            ResultAction.Delete.number,
            Intent().apply { putExtra(EXTRA_DELETE_POSITION, position) })
        finish()
    }

    fun changeColor(view: View) =
        binding.colorButton.setBackgroundColor((view.background as ColorDrawable).color)

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        priorityChoice = parent?.getItemAtPosition(position).toString()
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        if (isNewHabit)
            menuInflater.inflate(R.menu.menu_form_create_buttons, menu)
        else
            menuInflater.inflate(R.menu.menu_form_edit_buttons, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_save -> {
                if (isNewHabit)
                    addHabit()
                else
                    changeHabit()
                return true
            }

            R.id.action_delete -> {
                deleteHabit()
                return true
            }

            else -> {
                return super.onOptionsItemSelected(item)
            }
        }
    }
}