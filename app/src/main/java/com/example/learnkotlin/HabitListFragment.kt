package com.example.learnkotlin


import android.os.Bundle
import android.view.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.learnkotlin.adapters.HabitAdapter
import com.example.learnkotlin.databinding.FragmentHabitListBinding
import com.example.learnkotlin.models.HabitElement


class HabitListFragment : Fragment(), HabitAdapter.OnHabitListener {
    companion object {
        private const val ARGS_NAME = "habitList"

        fun newInstance(habitElements: ArrayList<HabitElement>): HabitListFragment {
            val bundle = Bundle().apply { putParcelableArrayList(ARGS_NAME, habitElements) }
            return HabitListFragment().apply { arguments = bundle }
        }
    }

    private lateinit var binding: FragmentHabitListBinding
    private lateinit var toggle: ActionBarDrawerToggle

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
        arguments?.apply { habitElements = getParcelableArrayList(ARGS_NAME) ?: habitElements }

        binding.addButton.setOnClickListener { onHabitClick(-1) }
        binding.recycleView.adapter = HabitAdapter(habitElements, this)
        binding.recycleView.layoutManager = LinearLayoutManager(activity)
    }

    private fun createToggle(
        activity: AppCompatActivity,
        drawerLayout: DrawerLayout
    ): ActionBarDrawerToggle {
        val toggle = ActionBarDrawerToggle(
            activity,
            drawerLayout,
            activity.findViewById(R.id.toolbar),
            R.string.drawer_open,
            R.string.drawer_close
        )

        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        return toggle
    }

    override fun onHabitClick(position: Int) {
        binding.addButton.hide()
        val habitElement =
            if (position < 0 || position >= habitElements.size) null else habitElements[position]

        toggle.isDrawerIndicatorEnabled = false
        toggle.setToolbarNavigationClickListener { activity?.supportFragmentManager?.popBackStack() }

        FragmentController.openDisplayFormFragment(
            (activity as AppCompatActivity),
            habitElement,
            position
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        (activity as AppCompatActivity).supportActionBar?.title = "List of habit"
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)

        val drawerLayout =
            (activity as AppCompatActivity).findViewById<DrawerLayout>(R.id.drawer_layout)
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
        toggle = createToggle(activity as AppCompatActivity, drawerLayout)
    }
}