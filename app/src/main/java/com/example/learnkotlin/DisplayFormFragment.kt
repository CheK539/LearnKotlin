package com.example.learnkotlin

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.RadioButton
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.drawToBitmap
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.learnkotlin.databinding.FragmentDisplayFormBinding
import com.example.learnkotlin.enums.HabitType
import com.example.learnkotlin.enums.PriorityType
import com.example.learnkotlin.interfaces.IDisplayFormCallback
import com.example.learnkotlin.models.HabitElement
import com.example.learnkotlin.viewModels.HabitElementViewModel


class DisplayFormFragment : Fragment(), AdapterView.OnItemSelectedListener {
    companion object {
        fun newInstance(habitElement: HabitElement, position: Int): DisplayFormFragment {
            val bundle = Bundle().apply {
                putParcelable(ARGS_HABIT_ELEMENT, habitElement)
                putInt(ARGS_POSITION, position)
            }
            return DisplayFormFragment().apply { arguments = bundle }
        }
    }

    private lateinit var binding: FragmentDisplayFormBinding

    private lateinit var arrayAdapter: ArrayAdapter<CharSequence>
    private lateinit var habitElementViewModel: HabitElementViewModel

    private var priorityChoice: String = ""
    private var isNewHabit = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)
        habitElementViewModel = ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return HabitElementViewModel(
                    arguments?.getParcelable(ARGS_HABIT_ELEMENT),
                    activity as IDisplayFormCallback?
                ) as T
            }
        }).get(HabitElementViewModel::class.java)
        habitElementViewModel.habit.observe(this, { fillEditForm(it) })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_display_form, container, false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        createArrayAdapter()
        addGlobalColorLayoutListener()
    }

    private fun createArrayAdapter() {
        arrayAdapter = ArrayAdapter.createFromResource(
            (activity as AppCompatActivity),
            R.array.priorities,
            R.layout.priority_item
        ).also { adapter ->
            binding.prioritySpinner.adapter = adapter
            binding.prioritySpinner.onItemSelectedListener = this
        }
    }

    private fun addGlobalColorLayoutListener() =
        binding.colorsLayout.viewTreeObserver.addOnGlobalLayoutListener(
            object : ViewTreeObserver.OnGlobalLayoutListener {
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
            view.setOnClickListener { changeColor(it) }
            val (x, y) = getCenterPosition(view, binding.colorsLayout)
            val pixel = bitmap.getPixel(x, y)
            view.setBackgroundColor(pixel)
        }
    }

    private fun changeColor(view: View) =
        binding.colorButton.setBackgroundColor((view.background as ColorDrawable).color)

    private fun getCenterPosition(view: View, layout: LinearLayout): Pair<Int, Int> {
        val rectanglePosition = IntArray(2)
        val layoutPosition = IntArray(2)
        view.getLocationOnScreen(rectanglePosition)
        layout.getLocationOnScreen(layoutPosition)
        val x = rectanglePosition[0] - layoutPosition[0] + view.width / 2
        val y = rectanglePosition[1] - layoutPosition[1] + view.height / 2
        return Pair(x, y)
    }

    private fun fillEditForm(habitElement: HabitElement?) {
        habitElement?.let { habit ->
            isNewHabit = false
            binding.titleEditText.setText(habit.title)
            binding.descriptionEditText.setText(habit.description)
            binding.completeCounter.setText(habit.completeCounter.toString())
            binding.periodNumber.setText(habit.periodNumber)
            binding.prioritySpinner.setSelection(arrayAdapter.getPosition(habit.priority.stringValue))
            binding.colorButton.setBackgroundColor(Color.parseColor(habit.color.split(" ")[0]))
            binding.typesGroup.check(
                if (habit.type == HabitType.Negative)
                    R.id.negativeType
                else R.id.positiveType
            )
        }
    }

    private fun createHabit(): HabitElement {
        val rgb = (binding.colorButton.background as ColorDrawable).color
        val hsv = FloatArray(3)
        Color.colorToHSV(rgb, hsv)
        val hexColor = "#${"%x".format(rgb)}"

        return HabitElement(
            binding.titleEditText.text.toString(),
            binding.descriptionEditText.text.toString(),
            PriorityType.fromString(priorityChoice),
            HabitType.fromString(
                view?.findViewById<RadioButton>(binding.typesGroup.checkedRadioButtonId)?.text
                    .toString()
            ),
            binding.completeCounter.text.toString().toIntOrNull() ?: 0,
            binding.periodNumber.text.toString(),
            "$hexColor ${hsv[0]} ${hsv[1]} ${hsv[2]}"
        )
    }

    private fun changeHabit() {
        val habitElement = createHabit()
        if (habitElementViewModel.validateHabit(habitElement))
            habitElementViewModel.setHabit(habitElement)
        else
            binding.textInputTitle.error = "Title cannot be empty."
    }

    private fun addHabit() {
        val habitElement = createHabit()
        if (habitElementViewModel.validateHabit(habitElement))
            habitElementViewModel.addHabit(habitElement)
        else
            binding.textInputTitle.error = "Title cannot be empty."
    }

    private fun deleteHabit() {
        habitElementViewModel.deleteHabit(createHabit())
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        priorityChoice = parent?.getItemAtPosition(position).toString()
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

        val drawerLayout = activity?.findViewById<DrawerLayout>(R.id.drawer_layout)
        drawerLayout?.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)

        if (isNewHabit)
            inflater.inflate(R.menu.menu_form_create_buttons, menu)
        else
            inflater.inflate(R.menu.menu_form_edit_buttons, menu)
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

    override fun onStop() {
        super.onStop()
        val inputMethodManager =
            activity?.getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(activity?.currentFocus?.windowToken, 0)
    }
}