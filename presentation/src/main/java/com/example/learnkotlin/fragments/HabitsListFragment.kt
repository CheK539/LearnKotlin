package com.example.learnkotlin.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.domain.enums.HabitType
import com.example.domain.models.Habit
import com.example.learnkotlin.R
import com.example.learnkotlin.adapters.HabitAdapter
import com.example.learnkotlin.controllers.FragmentController
import com.example.learnkotlin.databinding.FragmentHabitListBinding
import com.example.learnkotlin.viewModels.HabitsViewModel
import javax.inject.Inject


class HabitsListFragment : Fragment(), HabitAdapter.OnHabitListener,
    HabitAdapter.OnCompleteButtonListener {
    companion object {
        private const val ARGS_HABIT_TYPE = "habitType"
        fun newInstance(habitType: HabitType): HabitsListFragment {
            val bundle =
                Bundle().apply { putString(ARGS_HABIT_TYPE, habitType.typeString) }
            return HabitsListFragment().apply { arguments = bundle }
        }
    }

    private lateinit var binding: FragmentHabitListBinding

    @Inject
    lateinit var habitsViewModel: HabitsViewModel

    private var habitElements = mutableListOf<Habit>()
    private var habitType = HabitType.Positive

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (parentFragment as MainFragment).habitsComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_habit_list, container, false
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.apply {
            val habitTypeString = getString(ARGS_HABIT_TYPE)
            habitTypeString?.let { habitType = HabitType.fromString(it) }
        }

        habitsViewModel.habits.observe(viewLifecycleOwner, {
            habitElements.clear()
            habitElements.addAll(habitsViewModel.getHabitsByType(habitType))
            binding.recycleView.adapter?.notifyDataSetChanged()
        })

        binding.recycleView.adapter = HabitAdapter(habitElements, this, this)
        binding.recycleView.layoutManager = LinearLayoutManager(activity)
    }

    override fun onHabitClick(position: Int) {
        FragmentController.openDisplayFormFragment(findNavController(), habitElements[position].uid)
    }

    override fun onCompleteButtonClick(position: Int) {
        val habit = habitElements[position]
        val difference = habitsViewModel.increaseDoneCounter(habit.uid)
        val message = getCompeteMessage(habit.type, difference)

        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    private fun getCompeteMessage(habitType: HabitType, difference: Int): String {
        val timesPlural =
            resources.getQuantityString(R.plurals.times_plurals, difference, difference)

        return if (difference > 0) {
            val stringId = when (habitType) {
                HabitType.Positive -> R.string.left_complete_positive
                HabitType.Negative -> R.string.left_complete_negative
            }

            "${resources.getText(stringId)} $timesPlural"
        } else {
            val stringId = when (habitType) {
                HabitType.Positive -> R.string.complete_positive
                HabitType.Negative -> R.string.complete_negative
            }

            "${resources.getText(stringId)}"
        }
    }
}