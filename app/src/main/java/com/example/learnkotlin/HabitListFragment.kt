package com.example.learnkotlin


import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.learnkotlin.adapters.HabitAdapter
import com.example.learnkotlin.databinding.FragmentHabitListBinding
import com.example.learnkotlin.interfaces.IHabitListCallback
import com.example.learnkotlin.models.HabitElement


class HabitListFragment : Fragment(), HabitAdapter.OnHabitListener {
    companion object {
        private const val ARGS_HABIT_LIST = "habitList"

        fun newInstance(habitElements: ArrayList<HabitElement>): HabitListFragment {
            val bundle = Bundle().apply { putParcelableArrayList(ARGS_HABIT_LIST, habitElements) }
            return HabitListFragment().apply { arguments = bundle }
        }
    }

    private lateinit var binding: FragmentHabitListBinding
    private lateinit var callback: IHabitListCallback

    private var habitElements = arrayListOf<HabitElement>()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = parentFragment as IHabitListCallback
    }

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
            habitElements = getParcelableArrayList(ARGS_HABIT_LIST) ?: habitElements
        }

        binding.recycleView.adapter =
            HabitAdapter(habitElements, this)
        binding.recycleView.layoutManager = LinearLayoutManager(activity)
    }

    override fun onHabitClick(position: Int) {
        callback.onClickEnableBackUp()
        val habitElement =
            if (position < 0 || position >= habitElements.size) null else habitElements[position]

        FragmentController.openDisplayFormFragment(
            (activity as AppCompatActivity),
            habitElement,
            position
        )
    }
}