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
        fun newInstance(habitElements: ArrayList<HabitElement>): MainFragment {
            val bundle = Bundle().apply {
                putParcelableArrayList(ARGS_HABIT_ELEMENTS, habitElements)
            }
            return MainFragment().apply { arguments = bundle }
        }
    }

    private lateinit var binding: FragmentMainBinding
    private lateinit var habitsViewModel: HabitsViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        habitsViewModel = ViewModelProvider(this).get(HabitsViewModel::class.java)

        habitsViewModel.getHabits()
            //.observe(this, { binding.viewPager.adapter?.notifyDataSetChanged() })
            .observe(this, { setViewPagerAdapter() })
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
    }

    private fun setViewPagerAdapter() {
        val habitElements = habitsViewModel.getHabits().value ?: ArrayList()
        val positiveHabits =
            habitElements.filter { habitElement -> habitElement.type == HabitType.Positive }
        val negativeHabits =
            habitElements.filter { habitElement -> habitElement.type == HabitType.Negative }
        binding.viewPager.adapter = FragmentAdapter(
            childFragmentManager,
            lifecycle,
            positiveHabits as ArrayList<HabitElement>,
            negativeHabits as ArrayList<HabitElement>
        )
    }

    private fun addTabPage() {
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText(HabitType.Positive.typeString))
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText(HabitType.Negative.typeString))

        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                binding.viewPager.currentItem = tab?.position ?: 0
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