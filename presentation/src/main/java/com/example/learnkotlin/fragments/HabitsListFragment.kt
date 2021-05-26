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
import com.example.domain.usecases.FormUseCase
import com.example.domain.usecases.HabitsUseCase
import com.example.learnkotlin.R
import com.example.learnkotlin.adapters.HabitAdapter
import com.example.learnkotlin.applications.HabitApplication
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

    @Inject
    lateinit var habitsUseCase: HabitsUseCase

    @Inject
    lateinit var formUseCase: FormUseCase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (requireActivity().application as HabitApplication).habitsModule.inject(this)
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
        val message = habitsViewModel.increaseDoneCounter(habit.uid)

        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}