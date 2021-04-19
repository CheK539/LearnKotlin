package com.example.learnkotlin


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.example.learnkotlin.adapters.FragmentAdapter
import com.example.learnkotlin.databinding.FragmentMainBinding
import com.example.learnkotlin.enums.HabitType
import com.example.learnkotlin.models.HabitElement
import com.example.learnkotlin.viewModels.HabitsViewModel
import com.google.android.material.tabs.TabLayout


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

    private var positiveHabits: MutableList<HabitElement> = mutableListOf()
    private var negativeHabits: MutableList<HabitElement> = mutableListOf()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        habitsViewModel = ViewModelProvider(this).get(HabitsViewModel::class.java)

        habitsViewModel.getHabits()
            .observe(this, { setViewPagerAdapter() })
        /*.observe(this, { habitElements ->
            positiveHabits.clear()
            negativeHabits.clear()

            positiveHabits.addAll(habitElements.filter { habitElement -> habitElement.type == HabitType.Positive })
            negativeHabits.addAll(habitElements.filter { habitElement -> habitElement.type == HabitType.Negative })
            binding.viewPager.adapter?.notifyDataSetChanged()
        })*/
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
        setViewPagerAdapter()
        addTabPage()
    }

    private fun setViewPagerAdapter() {
        val habitElements = habitsViewModel.getHabits().value ?: ArrayList()
        positiveHabits.clear()
        negativeHabits.clear()
        positiveHabits.addAll(habitElements.filter { habitElement -> habitElement.type == HabitType.Positive })
        negativeHabits.addAll(habitElements.filter { habitElement -> habitElement.type == HabitType.Negative })
        binding.viewPager.adapter = FragmentAdapter(
            childFragmentManager,
            lifecycle,
            positiveHabits as ArrayList<HabitElement>,
            negativeHabits as ArrayList<HabitElement>
        )
        binding.viewPager.setCurrentItem(arguments?.getInt(ARGS_VIEWPAGER_POSITION) ?: 0, false)
    }

    private fun addTabPage() {
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText(HabitType.Positive.typeString))
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText(HabitType.Negative.typeString))

        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                binding.viewPager.currentItem = tab?.position ?: 0

                arguments = arguments?.apply {
                    putInt(ARGS_VIEWPAGER_POSITION, binding.viewPager.currentItem)
                } ?: Bundle().apply {
                    putInt(ARGS_VIEWPAGER_POSITION, binding.viewPager.currentItem)
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}

            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })

        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                binding.tabLayout.selectTab(binding.tabLayout.getTabAt(position))
            }
        })
    }

    private fun onAddButtonClick() {
        binding.addButton.hide()
        FragmentController.openDisplayFormFragment(
            activity as AppCompatActivity, null, -1
        )
    }
}