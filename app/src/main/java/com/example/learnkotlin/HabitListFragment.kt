package com.example.learnkotlin


import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.learnkotlin.adapters.HabitAdapter
import com.example.learnkotlin.databinding.FragmentHabitListBinding
import com.example.learnkotlin.models.HabitElement


class HabitListFragment : Fragment(), HabitAdapter.OnHabitListener {
    companion object {
        private const val ARGS_NAME = "HabitList"

        fun newInstance(habitElements: ArrayList<HabitElement>): HabitListFragment {
            val bundle = Bundle().apply { putParcelableArrayList(ARGS_NAME, habitElements) }
            return HabitListFragment().apply { arguments = bundle }
        }
    }

    var habitElements = arrayListOf<HabitElement>()
    lateinit var binding: FragmentHabitListBinding

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
        binding.recycleView.adapter =
            HabitAdapter(habitElements, this)
        binding.recycleView.layoutManager = LinearLayoutManager(activity)
    }

    override fun onHabitClick(position: Int) {
        binding.addButton.hide()
        val habitElement =
            if (position < 0 || position >= habitElements.size) null else habitElements[position]

        FragmentController.openDisplayFormFragment(activity, habitElement, position)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        (activity as AppCompatActivity).supportActionBar?.title = "List of habit"
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)
        (activity as AppCompatActivity).supportActionBar?.setDisplayShowHomeEnabled(false)
    }
}