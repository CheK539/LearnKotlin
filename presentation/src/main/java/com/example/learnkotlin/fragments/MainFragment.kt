package com.example.learnkotlin.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.domain.enums.HabitType
import com.example.learnkotlin.R
import com.example.learnkotlin.adapters.FragmentAdapter
import com.example.learnkotlin.applications.HabitApplication
import com.example.learnkotlin.di.components.HabitsComponent
import com.example.learnkotlin.controllers.FragmentController
import com.example.learnkotlin.databinding.FragmentMainBinding
import com.example.learnkotlin.viewModels.HabitsViewModel
import com.example.learnkotlin.viewsChanger.BottomSheetCallback
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.tabs.TabLayoutMediator
import javax.inject.Inject


class MainFragment : Fragment() {
    companion object {
        const val ARGS_VIEWPAGER_POSITION = "viewPagerPosition"
    }

    private lateinit var binding: FragmentMainBinding

    private var selectedItem = 0

    lateinit var habitsComponent: HabitsComponent

    @Inject
    lateinit var habitsViewModel: HabitsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val habitApplication = (requireActivity().application as HabitApplication)

        habitsComponent = habitApplication.applicationComponent.habitsComponent().create()
        habitsComponent.inject(this)

        habitsViewModel.habits.observe(this) {
            binding.viewPager.adapter?.notifyDataSetChanged()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_main, container, false
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        changeFragment()
    }

    private fun changeFragment() {
        binding.addButton.setOnClickListener { onAddButtonClick() }
        setViewPagerAdapter()
        addTabPage()
        setBottomSheetBehavior()
    }

    private fun setViewPagerAdapter() {
        binding.viewPager.adapter = FragmentAdapter(
            childFragmentManager,
            lifecycle,
            HabitType.Positive,
            HabitType.Negative
        )

        binding.viewPager.setCurrentItem(selectedItem, false)
    }

    private fun addTabPage() {
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            when (position) {
                0 -> {
                    tab.text = HabitType.Positive.typeString
                }
                1 -> {
                    tab.text = HabitType.Negative.typeString
                }
            }
        }.attach()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putInt(ARGS_VIEWPAGER_POSITION, binding.viewPager.currentItem)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)

        selectedItem = savedInstanceState?.getInt(ARGS_VIEWPAGER_POSITION) ?: 0
    }

    private fun setBottomSheetBehavior() {
        val bottomSheet = view?.findViewById<View>(R.id.filtersBottomSheet)
        bottomSheet?.let {
            val bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
            binding.viewPager.setPadding(0, 0, 0, bottomSheetBehavior.peekHeight)
            bottomSheetBehavior.addBottomSheetCallback(BottomSheetCallback(binding.viewPager))
        }
    }

    private fun onAddButtonClick() {
        binding.addButton.hide()
        FragmentController.openDisplayFormFragment(findNavController(), null)
    }
}