package com.example.learnkotlin


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.learnkotlin.adapters.HabitAdapter
import com.example.learnkotlin.databinding.FragmentHabitListBinding
import com.example.learnkotlin.models.HabitElement


class HabitListFragment : Fragment(), HabitAdapter.OnHabitListener {
    companion object {
        fun newInstance(habitElements: ArrayList<HabitElement>): HabitListFragment {
            val bundle =
                Bundle().apply { putParcelableArrayList(ARGS_HABIT_ELEMENT, habitElements) }
            return HabitListFragment().apply { arguments = bundle }
        }
    }

    private lateinit var binding: FragmentHabitListBinding

    private var habitElements = arrayListOf<HabitElement>()

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
            habitElements = getParcelableArrayList(ARGS_HABIT_ELEMENT) ?: habitElements
        }

        binding.recycleView.adapter = HabitAdapter(habitElements, this)
        binding.recycleView.layoutManager = LinearLayoutManager(activity)
    }

    override fun onHabitClick(position: Int) {
        val habitElement =
            if (position < 0 || position >= habitElements.size) null else habitElements[position]

        FragmentController.openDisplayFormFragment(findNavController(), habitElement)
    }
}