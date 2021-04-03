package com.example.learnkotlin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup


class DisplayFormFragment : Fragment() {
    companion object {
        private const val ARGS_NAME = "HabitList"

        fun newInstance(name: String): DisplayFormFragment {
            val bundle = Bundle().apply { putString(ARGS_NAME, name) }
            return DisplayFormFragment().apply { arguments = bundle }
        }
    }

    var name = ""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_display_form, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.apply { name = getString(ARGS_NAME, "") }
    }
}