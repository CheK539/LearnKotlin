package com.example.learnkotlin.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.learnkotlin.R
import com.example.learnkotlin.databinding.FragmentSearchBinding
import com.example.learnkotlin.viewModels.HabitsViewModel
import javax.inject.Inject

class SearchFragment : Fragment() {
    private lateinit var binding: FragmentSearchBinding

    @Inject
    lateinit var habitsViewModel: HabitsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (parentFragment as MainFragment).habitsComponent.inject(this)

        habitsViewModel.habits.observe(this) {
            habitsViewModel.sortedHabits?.let {
                binding.clearFilterButton.visibility = View.VISIBLE
            }
        }
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

        setViewListener()
    }

    private fun setViewListener() {
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
            override fun onQueryTextSubmit(query: String?) = false

            override fun onQueryTextChange(newText: String?): Boolean {
                habitsViewModel.searchHabits(newText)
                return true
            }
        })
    }
}