package com.example.learnkotlin

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.RadioButton
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.drawToBitmap
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.learnkotlin.databinding.FragmentDisplayFormBinding
import com.example.learnkotlin.interfaces.IDisplayFormCallback
import com.example.learnkotlin.models.HabitElement


class DisplayFormFragment : Fragment(), AdapterView.OnItemSelectedListener {
    companion object {
        private const val ARGS_HABIT_ELEMENT = "habitElement"
        private const val ARGS_POSITION = "position"

        fun newInstance(): DisplayFormFragment {
            return DisplayFormFragment()
        }

        fun newInstance(habitElement: HabitElement, position: Int): DisplayFormFragment {
            val bundle = Bundle().apply {
                putParcelable(ARGS_HABIT_ELEMENT, habitElement)
                putInt(ARGS_POSITION, position)
            }
            return DisplayFormFragment().apply { arguments = bundle }
        }
    }

    private lateinit var binding: FragmentDisplayFormBinding
    private lateinit var callback: IDisplayFormCallback
    private lateinit var arrayAdapter: ArrayAdapter<CharSequence>

    private var priorityChoice: String = ""
    private var isNewHabit = true
    private var position = -1


    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = activity as IDisplayFormCallback
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
        position = arguments?.getInt(ARGS_POSITION, position) ?: position

        if (position >= 0) {
            isNewHabit = false
            fillEditForm()
        }
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

    private fun fillEditForm() {
        val habit = arguments?.getParcelable<HabitElement>(ARGS_HABIT_ELEMENT) ?: return
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
            view?.findViewById<RadioButton>(binding.typesGroup.checkedRadioButtonId)?.text.toString(),
            binding.completeCounter.text.toString().toIntOrNull() ?: 0,
            binding.periodNumber.text.toString(),
            "$hexColor ${hsv[0]} ${hsv[1]} ${hsv[2]}"
        )
    }

    private fun changeHabit() {
        if (validateForm())
            callback.replaceHabit(createHabit(), position)
    }

    private fun addHabit() {
        if (validateForm())
            callback.addHabit(createHabit())
    }

    private fun deleteHabit() = callback.deleteHabit(position)

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        priorityChoice = parent?.getItemAtPosition(position).toString()
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        (activity as AppCompatActivity).supportActionBar?.title = "Add/edit habit"
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (activity as AppCompatActivity).supportActionBar?.setDisplayShowHomeEnabled(true)

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
}