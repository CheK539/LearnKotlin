package com.example.learnkotlin.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.learnkotlin.ARGS_HABIT_ELEMENTS
import com.example.learnkotlin.controllers.FragmentController
import com.example.learnkotlin.R
import com.example.learnkotlin.adapters.FragmentAdapter
import com.example.learnkotlin.databinding.FragmentMainBinding
import com.example.learnkotlin.enums.HabitType
import com.example.learnkotlin.models.HabitElement
import com.example.learnkotlin.viewModels.HabitsViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.tabs.TabLayoutMediator


class MainFragment : Fragment() {
    companion object {
        const val ARGS_VIEWPAGER_POSITION = "viewPagerPosition"
        fun newInstance(habitElements: ArrayList<HabitElement>): MainFragment {
            val bundle = Bundle().apply {
                putParcelableArrayList(ARGS_HABIT_ELEMENTS, habitElements)
            }
            return MainFragment().apply { arguments = bundle }
        }
    }

    private lateinit var binding: FragmentMainBinding
    private lateinit var habitsViewModel: HabitsViewModel

    private var selectedItem = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        habitsViewModel = ViewModelProvider(requireActivity(), object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return HabitsViewModel(activity!!.application) as T
            }
        }).get(HabitsViewModel::class.java)

        habitsViewModel.habits.observe(this, { binding.viewPager.adapter?.notifyDataSetChanged() })
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
            bottomSheetBehavior.addBottomSheetCallback(object :
                BottomSheetBehavior.BottomSheetCallback() {
                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    when (newState) {
                        BottomSheetBehavior.STATE_COLLAPSED -> binding.viewPager.setPadding(
                            0,
                            0,
                            0,
                            bottomSheetBehavior.peekHeight
                        )

                        BottomSheetBehavior.STATE_HIDDEN -> binding.viewPager.setPadding(0, 0, 0, 0)
                        BottomSheetBehavior.STATE_DRAGGING -> {
                        }
                        BottomSheetBehavior.STATE_EXPANDED -> {
                        }
                        BottomSheetBehavior.STATE_HALF_EXPANDED -> {
                        }
                        BottomSheetBehavior.STATE_SETTLING -> {
                        }
                    }
                }

                override fun onSlide(bottomSheet: View, slideOffset: Float) {
                    binding.viewPager.setPadding(
                        0,
                        0,
                        0,
                        (bottomSheet.height * slideOffset).toInt()
                    )
                }
            })
        }
    }

    private fun onAddButtonClick() {
        binding.addButton.hide()
        FragmentController.openDisplayFormFragment(findNavController(), null)
    }
}