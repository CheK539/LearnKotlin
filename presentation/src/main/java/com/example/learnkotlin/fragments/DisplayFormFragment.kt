package com.example.learnkotlin.fragments

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.RadioButton
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.learnkotlin.ARGS_HABIT_ELEMENT
import com.example.learnkotlin.R
import com.example.learnkotlin.controllers.FragmentController
import com.example.learnkotlin.databinding.FragmentDisplayFormBinding
import com.example.domain.enums.HabitType
import com.example.domain.enums.PriorityType
import com.example.domain.models.Habit
import com.example.domain.usecases.*
import com.example.learnkotlin.applications.HabitApplication
import com.example.learnkotlin.viewModels.FormViewModel
import com.example.learnkotlin.viewsChanger.ColorButtons
import javax.inject.Inject


class DisplayFormFragment : Fragment(), AdapterView.OnItemSelectedListener {
    companion object {
        fun newInstance(index: Int): DisplayFormFragment {
            val bundle = Bundle().apply {
                putInt(ARGS_HABIT_ELEMENT, index)
            }
            return DisplayFormFragment().apply { arguments = bundle }
        }
    }

    private lateinit var binding: FragmentDisplayFormBinding
    private lateinit var arrayAdapter: ArrayAdapter<CharSequence>
    private lateinit var formViewModel: FormViewModel
    private lateinit var navController: NavController

    private var drawerLayout: DrawerLayout? = null
    private var priorityChoice: String = ""
    private var isNewHabit = true

    @Inject
    lateinit var addHabitsUseCase: AddHabitsUseCase

    @Inject
    lateinit var updateHabitsUseCase: UpdateHabitsUseCase

    @Inject
    lateinit var deleteHabitsUseCase: DeleteHabitsUseCase

    @Inject
    lateinit var getByUidHabitsUseCase: GetByUidHabitsUseCase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)

        val uid = arguments?.getString(ARGS_HABIT_ELEMENT)
        isNewHabit = uid == null

        (requireActivity().application as HabitApplication).habitsModule.inject(this)

        formViewModel = ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return FormViewModel(
                    addHabitsUseCase,
                    updateHabitsUseCase,
                    deleteHabitsUseCase,
                    getByUidHabitsUseCase,
                    uid
                ) as T
            }
        }).get(FormViewModel::class.java)
        formViewModel.habit.observe(this, { fillEditForm(it) })
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

        navController = findNavController()
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
                    ColorButtons(binding.colorsLayout, binding.colorButton)
                }
            }
        )

    private fun fillEditForm(habitElement: Habit?) {
        habitElement?.let { habit ->
            binding.titleEditText.setText(habit.title)
            binding.descriptionEditText.setText(habit.description)
            binding.completeCounter.setText(habit.completeCounter.toString())
            binding.periodNumber.setText(habit.periodNumber.toString())
            binding.prioritySpinner.setSelection(arrayAdapter.getPosition(habit.priority.stringValue))

            val color = try {
                Color.parseColor(habitElement.color.split(" ")[0])
            } catch (e: Exception) {
                Color.parseColor("#ffffff")
            }

            binding.colorButton.setBackgroundColor(color)
            binding.typesGroup.check(
                if (habit.type == HabitType.Negative)
                    R.id.negativeType
                else R.id.positiveType
            )
        }
    }

    private fun createHabit(): Habit {
        val rgb = (binding.colorButton.background as ColorDrawable).color
        val hsv = FloatArray(3)
        Color.colorToHSV(rgb, hsv)
        val hexColor = "#${"%x".format(rgb)}"

        return Habit(
            binding.titleEditText.text.toString(),
            binding.descriptionEditText.text.toString(),
            PriorityType.fromString(priorityChoice),
            HabitType.fromString(
                view?.findViewById<RadioButton>(binding.typesGroup.checkedRadioButtonId)?.text
                    .toString()
            ),
            binding.completeCounter.text.toString().toIntOrNull() ?: 0,
            binding.periodNumber.text.toString().toIntOrNull() ?: 0,
            "$hexColor ${hsv[0]} ${hsv[1]} ${hsv[2]}"
        )
    }

    private fun changeHabit() {
        val habitElement = createHabit()
        if (formViewModel.validateHabit(habitElement)) {
            formViewModel.setHabit(habitElement)
            FragmentController.backToMainFragment(navController)
        } else {
            binding.textInputTitle.error = "Title cannot be empty."
            binding.descriptionEditText.error = "Description cannot be empty."
        }
    }

    private fun addHabit() {
        val habitElement = createHabit()
        if (formViewModel.validateHabit(habitElement)) {
            formViewModel.addHabit(habitElement)
            FragmentController.backToMainFragment(navController)
        } else {
            binding.textInputTitle.error = "Title cannot be empty."
            binding.descriptionEditText.error = "Description cannot be empty."
        }
    }

    private fun deleteHabit() {
        formViewModel.deleteHabit(createHabit())
        FragmentController.backToMainFragment(navController)
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        priorityChoice = parent?.getItemAtPosition(position).toString()
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

        drawerLayout = activity?.findViewById(R.id.drawer_layout)
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

    override fun onDestroy() {
        super.onDestroy()

        drawerLayout?.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
    }
}