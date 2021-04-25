package com.example.learnkotlin.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.learnkotlin.FragmentController
import com.example.learnkotlin.R
import com.example.learnkotlin.adapters.HabitAdapter
import com.example.learnkotlin.databinding.FragmentHabitListBinding
import com.example.learnkotlin.enums.HabitType
import com.example.learnkotlin.models.HabitElement
import com.example.learnkotlin.viewModels.HabitsViewModel


class HabitsListFragment : Fragment(), HabitAdapter.OnHabitListener {
    companion object {
        private const val ARGS_HABIT_TYPE = "habitType"
        fun newInstance(habitType: HabitType): HabitsListFragment {
            val bundle =
                Bundle().apply { putString(ARGS_HABIT_TYPE, habitType.typeString) }
            return HabitsListFragment().apply { arguments = bundle }
        }
    }

    private lateinit var binding: FragmentHabitListBinding

    private var habitElements = arrayListOf<HabitElement>()
    private val habitsViewModel by activityViewModels<HabitsViewModel>()
    private var habitType = HabitType.Positive

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        habitsViewModel.habits.observe(this, {
            habitElements.clear()
            habitElements.addAll(habitsViewModel.getHabitsByType(habitType))
            binding.recycleView.adapter?.notifyDataSetChanged()
        })
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

        habitElements = habitsViewModel.getHabitsByType(habitType)
        binding.recycleView.adapter = HabitAdapter(habitElements, this)
        binding.recycleView.layoutManager = LinearLayoutManager(activity)
    }

    override fun onHabitClick(position: Int) {
        val habitElement =
            if (position < 0 || position >= habitElements.size) null else habitElements[position]

        FragmentController.openDisplayFormFragment(findNavController(), habitElement)
    }
}