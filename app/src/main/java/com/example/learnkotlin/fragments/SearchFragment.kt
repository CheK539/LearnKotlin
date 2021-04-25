package com.example.learnkotlin.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.learnkotlin.R
import com.example.learnkotlin.databinding.FragmentSearchBinding
import com.example.learnkotlin.viewModels.HabitsViewModel

class SearchFragment : Fragment() {
    private lateinit var binding: FragmentSearchBinding
    private lateinit var habitsViewModel: HabitsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        habitsViewModel = ViewModelProvider(requireActivity(), object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return HabitsViewModel(activity!!.application) as T
            }
        }).get(HabitsViewModel::class.java)
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
                habitsViewModel.filterHabits(newText)
                return true
            }
        })
    }
}