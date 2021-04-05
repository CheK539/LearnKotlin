package com.example.learnkotlin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.learnkotlin.databinding.FragmentAboutBinding
import com.example.learnkotlin.models.HabitElement

class AboutFragment : Fragment() {
    companion object {
        private const val ARGS_NAME = "aboutPage"

        fun newInstance(): HabitListFragment {
            val bundle = Bundle()
            return HabitListFragment().apply { arguments = bundle }
        }
    }

    private lateinit var binding: FragmentAboutBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_about, container, false
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}