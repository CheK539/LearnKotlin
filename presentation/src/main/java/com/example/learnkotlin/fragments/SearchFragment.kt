package com.example.learnkotlin.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.domain.usecases.HabitsUseCase
import com.example.learnkotlin.R
import com.example.learnkotlin.applications.HabitApplication
import com.example.learnkotlin.databinding.FragmentSearchBinding
import com.example.learnkotlin.viewModels.HabitsViewModel
import javax.inject.Inject

class SearchFragment : Fragment() {
    private lateinit var binding: FragmentSearchBinding

    @Inject
    lateinit var habitsViewModel: HabitsViewModel

    @Inject
    lateinit var habitsUseCase: HabitsUseCase

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
            inflater, R.layout.fragment_search, container, false
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        habitsViewModel.habits.observe(viewLifecycleOwner) {
            habitsViewModel.sortedHabits?.let {
                binding.clearFilterButton.visibility = View.VISIBLE
            }
        }

        binding.ascendingSortButton.setOnClickListener {
            habitsViewModel.sortedByPriority(false)
            binding.clearFilterButton.visibility = View.VISIBLE
        }
        binding.descendingSortButton.setOnClickListener {
            habitsViewModel.sortedByPriority(true)
            binding.clearFilterButton.visibility = View.VISIBLE
        }
        binding.clearFilterButton.setOnClickListener {
            habitsViewModel.clearFilter()
            binding.clearFilterButton.visibility = View.INVISIBLE
        }
        binding.searchView.setOnQueryTextListener(object :
            android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                habitsViewModel.searchHabits(newText)
                return true
            }
        })
    }
}